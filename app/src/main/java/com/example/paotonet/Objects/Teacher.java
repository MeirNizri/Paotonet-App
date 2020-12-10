package com.example.paotonet.Objects;

public class Teacher {
    private String Email;
    private String password;


    public Teacher() {
    }

    public Teacher(String Email,String password) {

        this.Email = Email;
        this.password = password;

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