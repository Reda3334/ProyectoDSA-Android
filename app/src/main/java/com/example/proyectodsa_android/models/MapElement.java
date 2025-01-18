package com.example.proyectodsa_android.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MapElement {
    @Expose @SerializedName("id")
    String id;
    @Expose @SerializedName("elementId")
    private String elementId;
    @Expose @SerializedName("levelId")
    private String levelId;
    @Expose @SerializedName("x")
    private int x;
    @Expose @SerializedName("y")
    private int y;

    public MapElement(){}

    public MapElement(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }
}
