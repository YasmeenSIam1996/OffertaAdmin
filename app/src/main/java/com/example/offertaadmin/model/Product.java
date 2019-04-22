package com.example.offertaadmin.model;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private int order_id;
    private int product_id;
    private String updated_at;
    private String created_at;
    private int quantity;
    private double price;
    private String name;
    private String image;
    private double total_price;


    public int getId() {
        return id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public double getTotal_price() {
        return total_price;
    }
}
