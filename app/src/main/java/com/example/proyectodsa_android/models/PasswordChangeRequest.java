package com.example.proyectodsa_android.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PasswordChangeRequest {
    @SerializedName("oldPassword")
    @Expose
    private String oldPassword;

    @SerializedName("newPassword")
    @Expose
    private String newPassword;

    public PasswordChangeRequest() {}

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}