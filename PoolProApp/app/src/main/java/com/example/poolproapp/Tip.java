package com.example.poolproapp;
public class Tip {
    private long id;
    private String message;

    public Tip(long id, String message) {
        this.id = id;
        this.message = message;
    }

    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}