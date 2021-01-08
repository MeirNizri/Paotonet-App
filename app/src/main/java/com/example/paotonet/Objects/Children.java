package com.example.paotonet.Objects;

import java.util.ArrayList;

public class Children {
    ArrayList<Child> children;


    public Children() {
        this.children = new ArrayList<Child>();
    }
    public Children(ArrayList<Child> children) {
        this.children = children;
    }


    public ArrayList<Child> getChildren() {
        return children;
    }
    public void setChildren(ArrayList<Child> children) {
        this.children.addAll(children);
    }
    public void addChild(Child c){
        children.add(c);
    }
    public void deleteChild(Child c){
        children.remove(c);
    }
    public Child getChild(int id){
        for(Child c : children) {
            if(c.getId() == id)
                return c;
        }
        return null;
    }
}
