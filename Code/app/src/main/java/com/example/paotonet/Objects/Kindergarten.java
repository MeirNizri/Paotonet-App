package com.example.paotonet.Objects;

public class Kindergarten {
    int id;
    String name;
    String phone;
    String manager;
    String address;
    Children children;
    Messages messages;
    Reports reports;

    public Kindergarten() {
        this.children = new Children();
        this.messages = new Messages();
        this.reports = new Reports();
    }

    public Kindergarten(int id, String name, String phone, String manager, String address, Children children, Messages messages, Reports reports) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.manager = manager;
        this.address = address;
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
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getManager() {
        return manager;
    }
    public void setManager(String manager) {
        this.manager = manager;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
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