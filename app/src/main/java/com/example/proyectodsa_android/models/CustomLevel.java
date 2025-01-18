package com.example.proyectodsa_android.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomLevel {
    @Expose @SerializedName("id")
    private String id;
    @Expose @SerializedName("elements")
    private List<MapElement> elements;
    @Expose @SerializedName("levelName")
    private String levelName;
    @Expose @SerializedName("userName")
    private String userName;

    public CustomLevel(){}

    public List<MapElement> getElements() {
        return elements;
    }

    public void setElements(List<MapElement> elements) {
        this.elements = elements;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
