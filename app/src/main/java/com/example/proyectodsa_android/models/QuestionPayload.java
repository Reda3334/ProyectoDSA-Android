package com.example.proyectodsa_android.models;

import com.google.gson.annotations.Expose;

public class QuestionPayload {

    @Expose
    private String date;
    @Expose
    private String title;
    @Expose
    private String message;
    @Expose
    private String sender;
    @Expose
    private String username;

    public QuestionPayload(String date, String sender, String title, String message, String username) {
        this.date = date;
        this.message = message;
        this.sender = sender;
        this.title = title;
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public String getUsername() {
        return username;
    }
}
