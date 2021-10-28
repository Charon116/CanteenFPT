package com.example.canteenfpt.chefFoodPanel;

public class Chef {
    private String ConfirmPassword, Emailid, Fname, Lname, PhoneNumber, Password, Postcode;

    public Chef(String confirmPassword, String emailid, String fname, String lname, String phoneNumber, String password, String postcode) {
        ConfirmPassword = confirmPassword;
        Emailid = emailid;
        Fname = fname;
        Lname = lname;
        PhoneNumber = phoneNumber;
        Password = password;
        Postcode = postcode;
    }

    public Chef() {
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public String getEmailid() {
        return Emailid;
    }

    public String getFname() {
        return Fname;
    }

    public String getLname() {
        return Lname;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public String getPassword() {
        return Password;
    }

    public String getPostcode() {
        return Postcode;
    }
}
