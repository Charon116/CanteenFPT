package com.example.canteenfpt.chefFoodPanel;

public class ChefPendingOrders1 {

    private String GrandTotalPrice,MobileNumber,DishName,RandomUID;

    public ChefPendingOrders1(String grandTotalPrice, String mobileNumber, String dishName, String randomUID) {
        GrandTotalPrice = grandTotalPrice;
        MobileNumber = mobileNumber;
        DishName = dishName;
        RandomUID = randomUID;
    }

    public ChefPendingOrders1() {
    }

    public String getGrandTotalPrice() {
        return GrandTotalPrice;
    }

    public void setGrandTotalPrice(String grandTotalPrice) {
        GrandTotalPrice = grandTotalPrice;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getDishName() {
        return DishName;
    }

    public void setDishName(String dishName) {
        DishName = dishName;
    }

    public String getRandomUID() {
        return RandomUID;
    }

    public void setRandomUID(String randomUID) {
        RandomUID = randomUID;
    }
}
