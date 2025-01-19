package com.example.proyectodsa_android.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScoreData {
    @Expose @SerializedName("score")
    public int score;
    @Expose @SerializedName("level")
    public int level;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
