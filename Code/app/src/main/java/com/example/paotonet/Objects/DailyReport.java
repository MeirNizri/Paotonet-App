package com.example.paotonet.Objects;

import java.util.ArrayList;

public class DailyReport {
    private MyDate date;
    private ArrayList<Integer> presentId;

    public DailyReport() {
        this.presentId = new ArrayList<>();
        this.date = new MyDate();
    }
    public DailyReport(ArrayList<Integer> presentId) {
        this.presentId = presentId;
        this.date = new MyDate();
    }

    public ArrayList<Integer> getPresentId() {
        return presentId;
    }
    public void setPresentId(ArrayList<Integer> presentId) {
        this.presentId = presentId;
    }
    public void addChild(int id) {
        presentId.add(id);
    }
    public void deleteChild(int id) {
        for(int i=0; i<presentId.size(); i++) {
            if(presentId.get(i) == id)
                presentId.remove(i);
        }
    }
    public MyDate getDate() {
        return date;
    }
    public void setDate(MyDate date) {
        this.date = date;
    }
}