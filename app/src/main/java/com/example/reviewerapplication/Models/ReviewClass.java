package com.example.reviewerapplication.Models;

public class ReviewClass {

    String detailedReview, shortReview, rating, user, itemName;

    public ReviewClass() {

    }

    public ReviewClass(String detailedReview, String shortReview, String rating, String user, String itemName) {
        this.detailedReview = detailedReview;
        this.shortReview = shortReview;
        this.rating = rating;
        this.user = user;
        this.itemName = itemName;
    }

    public String getDetailedReview() {
        return detailedReview;
    }

    public String getShortReview() {
        return shortReview;
    }

    public String getRating() {
        return rating;
    }

    public String getUser() {
        return user;
    }

    public String getItemName() {
        return itemName;
    }

}
