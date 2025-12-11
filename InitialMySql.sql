-- initial mysql db
sudo apt update
sudo apt install mariadb-server -y
  
sudo systemctl start mariadb
sudo systemctl enable mariadb
  
sudo mysql_secure_installation
  
-- create database
CREATE DATABASE IF NOT EXISTS market_db
USE market_db;

-- reset tables (for testing)
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS invoice_items;
DROP TABLE IF EXISTS invoices;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
SET FOREIGN_KEY_CHECKS = 1;

-- categories table
CREATE TABLE categories (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,        -- category name
  description VARCHAR(255) DEFAULT NULL, -- simple text
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uq_categories_name (name) -- unique name
) ENGINE=InnoDB DEFAULT CHARSET=ascii;

-- products table
CREATE TABLE products (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(150) NOT NULL,        -- product name
  price DECIMAL(10,2) NOT NULL,      -- product price
  quantity INT NOT NULL DEFAULT 0,   -- stock
  category_id INT UNSIGNED NOT NULL, -- link to category
  sku VARCHAR(50) DEFAULT NULL,      -- product code
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  INDEX idx_products_category (category_id),
  CONSTRAINT fk_products_category FOREIGN KEY (category_id)
    REFERENCES categories(id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=ascii;

-- invoices header table
CREATE TABLE invoices (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  invoice_number VARCHAR(40) NOT NULL UNIQUE, -- invoice id text
  date_created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  customer_name VARCHAR(150) DEFAULT NULL, -- customer
  cashier_name VARCHAR(100) DEFAULT NULL,  -- cashier
  total DECIMAL(12,2) NOT NULL DEFAULT 0.00, -- invoice total
  status ENUM('OPEN','PAID','CANCELLED') NOT NULL DEFAULT 'OPEN', -- invoice status
  PRIMARY KEY (id),
  INDEX idx_invoices_number (invoice_number)
) ENGINE=InnoDB DEFAULT CHARSET=ascii;

-- invoice items table
CREATE TABLE invoice_items (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  invoice_id INT UNSIGNED NOT NULL,  -- link to invoice
  product_id INT UNSIGNED NOT NULL,  -- link to product
  quantity INT NOT NULL,             -- sold qty
  price DECIMAL(10,2) NOT NULL,      -- item price
  line_total DECIMAL(12,2) NOT NULL, -- qty * price
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  INDEX idx_items_invoice (invoice_id),
  INDEX idx_items_product (product_id),
  CONSTRAINT fk_items_invoice FOREIGN KEY (invoice_id)
    REFERENCES invoices(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_items_product FOREIGN KEY (product_id)
    REFERENCES products(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=ascii;

-- sample categories
INSERT INTO categories (name, description) VALUES
  ('Meat', 'Fresh and frozen meat'),
  ('Dairy', 'Milk and cheese'),
  ('Legumes', 'Beans and grains'),
  ('Vegetables', 'Fresh vegetables');

-- sample products
INSERT INTO products (name, price, quantity, category_id, sku) VALUES
  ('Beef 1kg', 350.00, 20, 1, 'MEAT-BEEF-1KG'),
  ('Chicken 1.2kg', 120.50, 35, 1, 'MEAT-CHICKEN-1.2KG'),
  ('Milk 1L', 25.00, 100, 2, 'DAIRY-MILK-1L'),
  ('Cheese 400g', 80.00, 40, 2, 'DAIRY-CHEESE-400G'),
  ('Red Lentils 1kg', 45.00, 50, 3, 'LEGUMES-LENTIL-1KG'),
  ('Onion 1kg', 10.00, 200, 4, 'VEG-ONION-1KG');

-- sample invoices
INSERT INTO invoices (invoice_number, customer_name, cashier_name, total, status) VALUES
  ('2025-00001', 'Ali', 'Cashier1', 0.00, 'OPEN'),
  ('2025-00002', 'Mohamed', 'Cashier2', 0.00, 'OPEN');

-- triggers (simple logic)
DELIMITER $$

-- before insert item (calculate total)
CREATE TRIGGER bi_invoice_items BEFORE INSERT ON invoice_items
FOR EACH ROW
BEGIN
  -- qty must be > 0
  IF NEW.quantity <= 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Bad quantity';
  END IF;
  -- price must be >= 0
  IF NEW.price < 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Bad price';
  END IF;
  -- calculate line total
  SET NEW.line_total = NEW.quantity * NEW.price;
END$$

-- after insert item (update stock + invoice total)
CREATE TRIGGER ai_invoice_items AFTER INSERT ON invoice_items
FOR EACH ROW
BEGIN
  DECLARE stock INT;

  -- get product stock
  SELECT quantity INTO stock FROM products WHERE id = NEW.product_id FOR UPDATE;

  -- no product
  IF stock IS NULL THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Product not found';
  END IF;

  -- no enough stock
  IF stock < NEW.quantity THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Stock too low';
  END IF;

  -- reduce stock
  UPDATE products SET quantity = quantity - NEW.quantity WHERE id = NEW.product_id;

  -- add to invoice total
  UPDATE invoices SET total = total + NEW.line_total WHERE id = NEW.invoice_id;
END$$

-- before update item (simple checks)
CREATE TRIGGER bu_invoice_items BEFORE UPDATE ON invoice_items
FOR EACH ROW
BEGIN
  IF NEW.quantity <= 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Bad quantity';
  END IF;
  IF NEW.price < 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Bad price';
  END IF;
  SET NEW.line_total = NEW.quantity * NEW.price;
END$$

-- after update item (update stock + invoice total diff)
CREATE TRIGGER au_invoice_items AFTER UPDATE ON invoice_items
FOR EACH ROW
BEGIN
  DECLARE old_stock INT;

  -- if product changed
  IF OLD.product_id <> NEW.product_id THEN
    -- return old qty
    UPDATE products SET quantity = quantity + OLD.quantity WHERE id = OLD.product_id;

    -- check new product stock
    SELECT quantity INTO old_stock FROM products WHERE id = NEW.product_id FOR UPDATE;

    IF old_stock < NEW.quantity THEN
      SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Stock too low';
    END IF;

    -- reduce new stock
    UPDATE products SET quantity = quantity - NEW.quantity WHERE id = NEW.product_id;

  ELSE
    -- same product: adjust stock difference
    UPDATE products
      SET quantity = quantity + (OLD.quantity - NEW.quantity)
      WHERE id = NEW.product_id;

    SELECT quantity INTO old_stock FROM products WHERE id = NEW.product_id;

    IF old_stock < 0 THEN
      SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Stock too low';
    END IF;
  END IF;

  -- update invoice total
  UPDATE invoices
    SET total = total + (NEW.line_total - OLD.line_total)
    WHERE id = NEW.invoice_id;
END$$

-- after delete item (return stock + remove from total)
CREATE TRIGGER ad_invoice_items AFTER DELETE ON invoice_items
FOR EACH ROW
BEGIN
  -- return stock
  UPDATE products SET quantity = quantity + OLD.quantity WHERE id = OLD.product_id;

  -- reduce invoice total
  UPDATE invoices SET total = total - OLD.line_total WHERE id = OLD.invoice_id;
END$$

-- protect products (no negative values)
CREATE TRIGGER bu_products BEFORE UPDATE ON products
FOR EACH ROW
BEGIN
  IF NEW.quantity < 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Negative stock';
  END IF;
  IF NEW.price < 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Negative price';
  END IF;
END$$

DELIMITER ;
