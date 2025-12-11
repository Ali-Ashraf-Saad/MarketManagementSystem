package com.MMS.model;

import java.util.ArrayList;
import java.util.List;

public class Invoice {
    private int id;
    private String invoiceNumber;
    private String customerName;
    private String cashierName;
    private double total;
    private String status;

    private List<InvoiceItem> items = new ArrayList<>();

    public int getId(){return id;}
    public void setId(int id){this.id=id;}

    public String getInvoiceNumber(){return invoiceNumber;}
    public void setInvoiceNumber(String invoiceNumber){this.invoiceNumber=invoiceNumber;}

    public String getCustomerName(){return customerName;}
    public void setCustomerName(String customerName){this.customerName=customerName;}

    public String getCashierName(){return cashierName;}
    public void setCashierName(String cashierName){this.cashierName=cashierName;}

    public double getTotal(){return total;}
    public void setTotal(double total){this.total=total;}

    public String getStatus(){return status;}
    public void setStatus(String status){this.status=status;}

    public List<InvoiceItem> getItems(){return items;}
}