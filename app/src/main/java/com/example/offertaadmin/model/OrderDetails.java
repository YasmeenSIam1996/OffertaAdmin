package com.example.offertaadmin.model;

import java.io.Serializable;
import java.util.List;

public class OrderDetails implements Serializable {
    private String user_name;
    private String user_phone1;
    private String user_phone2;
    private String destination;
    private String payment;
    private double price;
    private double shipping;
    private String copoun;
    private double total_price;
    private String tax;
    private String date;

    private List<Product> products;

    public String getUser_name() {
        return user_name;
    }

    public String getUser_phone1() {
        return user_phone1;
    }

    public String getUser_phone2() {
        return user_phone2;
    }

    public String getDestination() {
        return destination;
    }

    public String getPayment() {
        return payment;
    }

    public double getPrice() {
        return price;
    }

    public double getShipping() {
        return shipping;
    }

    public String getCopoun() {
        return copoun;
    }

    public double getTotal_price() {
        return total_price;
    }

    public String getTax() {
        return tax;
    }

    public List<Product> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "user_name='" + user_name + '\'' +
                ", user_phone1='" + user_phone1 + '\'' +
                ", user_phone2='" + user_phone2 + '\'' +
                ", destination='" + destination + '\'' +
                ", payment='" + payment + '\'' +
                ", price=" + price +
                ", shipping=" + shipping +
                ", copoun='" + copoun + '\'' +
                ", total_price=" + total_price +
                ", tax='" + tax + '\'' +
                '}';
    }


    public String getDate() {
        return date;
    }
}
