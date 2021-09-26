package com.example.testapp;

public class MainModel {
    String ItemName,ItemDescription,ItemAuthor,url;

    MainModel(){

    }

    public MainModel(String itemName, String itemDescription, String itemAuthor, String url) {
        ItemName = itemName;
        ItemDescription = itemDescription;
        ItemAuthor = itemAuthor;
        this.url = url;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public void setItemDescription(String itemDescription) {
        ItemDescription = itemDescription;
    }

    public String getItemAuthor() {
        return ItemAuthor;
    }

    public void setItemAuthor(String itemAuthor) {
        ItemAuthor = itemAuthor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
