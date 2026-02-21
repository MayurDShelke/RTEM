package com.mayurshelke.rtem5;

public class CartItem {
    private String food;
    private int cost;

    public CartItem(String food, int cost) {
        this.food = food;
        this.cost = cost;
    }

    public String getFood() {
        return food;
    }

    public int getCost() {
        return cost;
    }
}
