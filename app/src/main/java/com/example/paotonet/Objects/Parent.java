package com.example.paotonet.Objects;

public class Parent {
    private int id;
    private String Email;
    private String password;


    public Parent() {
    }

    public Parent(int id, String Email,String password) {
        this.id = id;
        this.Email = Email;
        this.password = password;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String name) {
        this.password = password;
    }

    public String getEmail() { return Email; }

    public void setImg(String Email) { this.Email = Email; }


}
