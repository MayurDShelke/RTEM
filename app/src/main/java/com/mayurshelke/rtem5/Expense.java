package com.mayurshelke.rtem5;

public class Expense {
    private String tripTitle;
    private double distance;
    private double mileage;
    private double petrolPrice;
    private double tollExpense;
    private double foodCost;
    private double totalExpense;

    public Expense() {
        // Required empty constructor for Firebase
    }



    public Expense(String tripTitle, double distance, double mileage, double petrolPrice,
                   double tollExpense, double foodCost, double totalExpense) {
        this.tripTitle = tripTitle;
        this.distance = distance;
        this.mileage = mileage;
        this.petrolPrice = petrolPrice;
        this.tollExpense = tollExpense;
        this.foodCost = foodCost;
        this.totalExpense = totalExpense;
    }

    public String getTripTitle() {
        return tripTitle;
    }

    public void setTripTitle(String tripTitle) {
        this.tripTitle = tripTitle;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public double getPetrolPrice() {
        return petrolPrice;
    }

    public void setPetrolPrice(double petrolPrice) {
        this.petrolPrice = petrolPrice;
    }

    public double getTollExpense() {
        return tollExpense;
    }

    public void setTollExpense(double tollExpense) {
        this.tollExpense = tollExpense;
    }

    public double getFoodCost() {
        return foodCost;
    }

    public void setFoodCost(double foodCost) {
        this.foodCost = foodCost;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }
}
