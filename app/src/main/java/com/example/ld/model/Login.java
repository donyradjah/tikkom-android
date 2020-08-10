package com.example.ld.model;

import com.google.gson.annotations.SerializedName;

public class Login {
    @SerializedName("token")
    String token;

    @SerializedName("success")
    boolean success;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
