package com.MMS.model;

public class Product {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private int categoryId;

    public Product(){}
    public Product(int id,String name,double price,int qty,int cat){
        this.id=id;this.name=name;this.price=price;this.quantity=qty;this.categoryId=cat;
    }

    public int getId(){return id;}
    public void setId(int id){this.id=id;}

    public String getName(){return name;}
    public void setName(String name){this.name=name;}

    public double getPrice(){return price;}
    public void setPrice(double price){this.price=price;}

    public int getQuantity(){return quantity;}
    public void setQuantity(int quantity){this.quantity=quantity;}

    public int getCategoryId(){return categoryId;}
    public void setCategoryId(int categoryId){this.categoryId=categoryId;}
}