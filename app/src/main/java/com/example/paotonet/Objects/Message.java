package com.example.paotonet.Objects;

public class Message {
    String sender;
    String content;
    MyDate time;

    public Message() {
    }
    public Message(String sender, String content, MyDate time) {
        this.sender = sender;
        this.content = content;
        this.time = time;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String message) {
        this.content = message;
    }
    public MyDate getTime() {
        return time;
    }
    public void setTime(MyDate time) {
        this.time = time;
    }
    public String toDateString() {
        return time.toString();
    }
    public String toTimeString() {
        return time.toTimeString();
    }
    public String toDateAndTimeString() {
        return time.toString();
    }
    @Override
    public String toString() {
        return "From: " + sender + "\n" + content + ".\n" + time.toTimeString();
    }
}