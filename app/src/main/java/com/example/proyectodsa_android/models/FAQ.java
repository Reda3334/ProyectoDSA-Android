package com.example.proyectodsa_android.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FAQ {
    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("question")
    @Expose
    private String question;

    @SerializedName("answer")
    @Expose
    private String answer;

    @SerializedName("sender")
    @Expose
    private String sender;

    public FAQ() {}

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
}
