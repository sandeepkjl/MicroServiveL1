package com.wipro.monolothic;

public class Trade {



    private String ticker;
    private double price;
    private int qty;
    private double totalCost;


    public Trade() {
        super();
    }

    public Trade(String ticker, double price, int qty, double totalCost) {
        this.ticker = ticker;
        this.price = price;
        this.qty = qty;
        this.totalCost = totalCost;
    }

    public Trade(String ticker, double price, int qty) {
        this.ticker = ticker;
        this.price = price;
        this.qty = qty;
        this.totalCost = totalCost;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}
