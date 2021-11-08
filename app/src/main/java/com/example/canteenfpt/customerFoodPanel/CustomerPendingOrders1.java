package com.example.canteenfpt.customerFoodPanel;

public class CustomerPendingOrders1 {
    private String GrandTotalPrice,MobileNumber,Name;

    public CustomerPendingOrders1(String grandTotalPrice, String mobileNumber, String name) {
        GrandTotalPrice = grandTotalPrice;
        MobileNumber = mobileNumber;
        Name = name;
    }

    public CustomerPendingOrders1() {
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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
