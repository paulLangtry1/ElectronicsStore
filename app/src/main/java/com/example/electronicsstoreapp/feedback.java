package com.example.electronicsstoreapp;

public class feedback
{
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    String category;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getReviewid() {
        return reviewid;
    }

    public void setReviewid(String reviewid) {
        this.reviewid = reviewid;
    }

    String title;
    String manufacturer;


    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    String rating;
    String content;
    String userid;
    String reviewid;

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    String itemid;

    public feedback()
    {

    }


    public feedback(String category,String title, String manufacturer, String rating,String content,String userid,String reviewid,String itemid)
    {
        this.category = category;
        this.title = title;
        this.manufacturer = manufacturer;
        this.rating = rating;
        this.content = content;
        this.userid = userid;
        this.reviewid=reviewid;
        this.itemid = itemid;
    }
}
