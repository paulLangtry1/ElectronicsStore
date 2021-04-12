package com.example.electronicsstoreapp;

public class Customer
{
    private String email;
    private String password;
    private String username;

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    private int discount;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String address;
    private String userID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }






    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    private String phoneNo;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Customer()
    {

    }


    public Customer(String email, String password, String username,String phoneNo,String userID,String address,int discount)
    {
        this.email = email;
        this.password = password;
        this.username = username;
        this.phoneNo = phoneNo;
        this.address = address;

        this.userID = userID;
        this.discount = discount;

    }
}
