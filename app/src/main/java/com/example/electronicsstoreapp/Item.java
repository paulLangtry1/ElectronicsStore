package com.example.electronicsstoreapp;

public class Item
{
    String category;
    String manufacturer;
    String title;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getItemurl() {
        return itemurl;
    }

    public void setItemurl(String itemurl) {
        this.itemurl = itemurl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    int quantity;
    String price;
    String itemurl;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    String userid;

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    String itemid;

    public Item()
    {

    }


    public Item(String category, String manufacturer, String title,int quantity,String price,String itemurl,String itemid,String userid)
    {
        this.category = category;
        this.manufacturer = manufacturer;
        this.title = title;
        this.quantity = quantity;
        this.price = price;
        this.itemurl=itemurl;
        this.itemid = itemid;
        this.userid = userid;

    }

}
