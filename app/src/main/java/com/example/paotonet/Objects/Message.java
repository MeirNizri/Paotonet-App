package com.example.paotonet.Objects;

public class Message {
    String title;
    String sender;
    String destination;
    String content;
    MyDate time;

    public Message() {
    }
    public Message(String title, String sender, String destination, String content, MyDate time) {
        this.title = title;
        this.sender = sender;
        this.destination = destination;
        this.content = content;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
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
        return time.toDateString();
    }
    public String toTimeString() {
        return time.toTimeString();
    }
    public String toTimeAndDateString() {
        return time.toTimeAndDateString();
    }
    @Override
    public String toString() {
        return "Title: "+title+"\n" + "From: "+sender+ ", To:"+destination+"\n " + content+".\n " + time.toTimeString();
    }
}