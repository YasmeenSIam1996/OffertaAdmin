package com.example.offertaadmin.model;

import java.io.Serializable;

public class OrderStatus implements Serializable {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public String getStatus() {
        return name;
    }
}
