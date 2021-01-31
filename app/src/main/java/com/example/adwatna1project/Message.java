package com.example.adwatna1project;

public class Message {

    private String from , message, to;
    private boolean isseen;

    public Message() {
    }

    public Message(String from, String message, String to, boolean isseen) {
        this.from = from;
        this.message = message;
        this.to = to;
        this.isseen = isseen;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }
}
