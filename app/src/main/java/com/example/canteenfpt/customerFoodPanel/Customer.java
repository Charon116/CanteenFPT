package com.example.canteenfpt.customerFoodPanel;

public class Customer {
    private String Password,ConfirmPassword,EmailId,PhoneNumber;

    public Customer() {
    }

    public Customer(String password, String confirmPassword, String emailId, String phoneNumber) {
        Password = password;
        ConfirmPassword = confirmPassword;
        EmailId = emailId;
        PhoneNumber = phoneNumber;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
}
