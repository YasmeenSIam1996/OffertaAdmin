package com.example.offertaadmin.model;

import java.io.Serializable;

public class Order implements Serializable {
    private int id;
    private String user_name;
    private String date;
    private int status;
    private String status_text;


    public int getId() {
        return id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getDate() {
        return date;
    }

    public int getStatus() {
        return status;
    }

    public String getStatus_text() {
        return status_text;
    }
}
