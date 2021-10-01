package com.example.reviewerapplication.Models;

public class ItemClass {

    public String category, imageURL, itemName, description;
    int reviews;

    public ItemClass(){

    }

    public ItemClass(String name, String imageURL, String category, String description, int reviews){
        this.itemName = name;
        this.imageURL = imageURL;
        this.category = category;
        this.description = description;
        this.reviews = reviews;
    }

    public void display(){
        System.out.println(itemName + ", " + category + ", " + reviews);
    }

    public String getImageURL(){
        return imageURL;
    }

    public String getItemName() {
        return itemName;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public int getReviews() {
        return reviews;
    }

}
