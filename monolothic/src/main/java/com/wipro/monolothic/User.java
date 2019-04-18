package com.wipro.monolothic;

public class User {


    private String userId;
    private String password;
    private String email;
    private double balance;

    public User() {
    }

    public User(String userId, String password, String  email) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.balance=100000;

    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getBlanace() {
        return balance;
    }

    public void setBalance(double price) {
        this.balance = price;
    }
}
