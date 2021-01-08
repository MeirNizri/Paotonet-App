package com.example.paotonet.Objects;

public class Kindergarten {
    int id;
    Children children;
    Messages messages;
    Reports reports;

    public Kindergarten() {
        this.id = 0;
        this.children = new Children();
        this.messages = new Messages();
        this.reports = new Reports();
    }
    public Kindergarten(int id, Children children, Messages messages, Reports reports) {
        this.id = id;
        this.children = children;
        this.messages = messages;
        this.reports = reports;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Messages getMessages() {
        return messages;
    }
    public void setMessages(Messages messages) {
        this.messages = messages;
    }
    public Children getChildren() {
        return children;
    }
    public void setChildren(Children children) {
        this.children = children;
    }
    public Reports getReports() {
        return reports;
    }
    public void setReports(Reports reports) {
        this.reports = reports;
    }
}