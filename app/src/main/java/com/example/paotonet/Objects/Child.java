package com.example.paotonet.Objects;

public class Child {
    private int id;
    private String name;
    private String img;
    private String lastSignedDeclaration;
    private MyDate birthDate;

    public Child() {
    }

    public Child(int id, String name, String img, String lastSignedDeclaration, MyDate birthDate) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.lastSignedDeclaration = lastSignedDeclaration;
        this.birthDate = birthDate;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLastSignedDeclaration() {
        return lastSignedDeclaration;
    }

    public void setLastSignedDeclaration(String lastSignedDeclaration) {
        this.lastSignedDeclaration = lastSignedDeclaration;
    }

    public MyDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(MyDate birthDate) {
        this.birthDate = birthDate;
    }
}