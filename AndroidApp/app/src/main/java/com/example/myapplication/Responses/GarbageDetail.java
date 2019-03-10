package com.example.myapplication.Responses;

import com.google.gson.annotations.SerializedName;

public class GarbageDetail {
    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }
}
