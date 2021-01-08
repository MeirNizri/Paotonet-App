package com.example.paotonet.Objects;

public class Parent {
    String name;
    String email;
    int kindergartenId;
    int childId;
    Messages messages;

    public Parent() {
        super();
    }
    public Parent(String name, String email, int kindergartenId, int childId) {
        this.name = name;
        this.email = email;
        this.kindergartenId = kindergartenId;
        this.childId = childId;
    }
    public Parent(String name, String email, int kindergartenId, int childId, Messages messages) {
        this.name = name;
        this.email = email;
        this.kindergartenId = kindergartenId;
        this.childId = childId;
        this.messages = messages;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getKindergartenId() {
        return kindergartenId;
    }
    public void setKindergartenId(int kindergartenId) {
        this.kindergartenId = kindergartenId;
    }
    public int getChildId() {
        return childId;
    }
    public void setChildId(int childId) {
        this.childId = childId;
    }
    public Messages getMessages() {
        return messages;
    }
    public void setMessages(Messages messages) {
        this.messages = messages;
    }
}