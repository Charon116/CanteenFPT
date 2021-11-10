package com.example.canteenfpt.customerFoodPanel;

public class CustomerPaymentOrders1 {

    private String GrandTotalPrice,MobileNumber,RandomUID;

    public CustomerPaymentOrders1(String grandTotalPrice, String mobileNumber, String randomUID) {
        GrandTotalPrice = grandTotalPrice;
        MobileNumber = mobileNumber;
        RandomUID = randomUID;
    }

    public CustomerPaymentOrders1() {
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

    public String getRandomUID() {
        return RandomUID;
    }

    public void setRandomUID(String randomUID) {
        RandomUID = randomUID;
    }
}
