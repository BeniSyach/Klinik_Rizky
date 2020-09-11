package com.example.klinikrizky.model;



public class pesan {
    private String Title;
    private String Message;

    public pesan(String title, String message) {
        Title = title;
        Message = message;
    }

    public pesan() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
