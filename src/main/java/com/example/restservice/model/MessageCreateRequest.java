package com.example.restservice.model;

public class MessageCreateRequest {

    public String user_name;
    public String latitude;
    public String longitude;
    public String text;

    public String getUser_name() {
        return user_name;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getText() {
        return text;
    }
}
