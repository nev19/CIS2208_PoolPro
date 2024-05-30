package com.example.poolproapp;
public class Pool {
    private long id;
    private String name;
    private String owner;
    private String phone;
    private double capacity;

    public Pool(long id, String name, String owner, String phone, double capacity){
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.phone = phone;
        this.capacity = capacity;
    }

    public long getId() {
        return id;
    }




    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }
}
