package com.example.reviewerapplication;

public class UserModel {
    private String name;
    private String email;
    private String phoneNo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }


    public UserModel(){
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
    }

    public UserModel(String name, String email, String phoneNo){
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
    }
}
