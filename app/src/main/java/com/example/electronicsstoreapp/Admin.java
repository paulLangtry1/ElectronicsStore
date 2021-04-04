package com.example.electronicsstoreapp;

public class Admin
{

    private String email;
    private String password;
    private String username;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String address;

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    private String adminID;


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

    public Admin()
    {

    }


    public Admin(String email, String password, String username)
    {
        this.email = email;
        this.password = password;
        this.username = username;

    }
}
