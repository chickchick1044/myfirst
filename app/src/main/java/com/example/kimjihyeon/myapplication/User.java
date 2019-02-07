package com.example.kimjihyeon.myapplication;

public class User {
    private String uid, email, name, profileUrl;

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getProfileUrl(){
        return profileUrl;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
