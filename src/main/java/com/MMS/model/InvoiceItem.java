package com.MMS.model;

public class InvoiceItem {
    private int id;
    private int invoiceId;
    private int productId;
    private int quantity;
    private double price;
    private double lineTotal;

    public int getId(){return id;}
    public void setId(int id){this.id=id;}

    public int getInvoiceId(){return invoiceId;}
    public void setInvoiceId(int invoiceId){this.invoiceId=invoiceId;}

    public int getProductId(){return productId;}
    public void setProductId(int productId){this.productId=productId;}

    public int getQuantity(){return quantity;}
    public void setQuantity(int quantity){this.quantity=quantity;}

    public double getPrice(){return price;}
    public void setPrice(double price){this.price=price;}

    public double getLineTotal(){return lineTotal;}
    public void setLineTotal(double lineTotal){this.lineTotal=lineTotal;}
}