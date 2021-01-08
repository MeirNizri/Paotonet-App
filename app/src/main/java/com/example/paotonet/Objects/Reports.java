package com.example.paotonet.Objects;

import java.util.ArrayList;

public class Reports {
    ArrayList<DailyReport> reports;


    public Reports() {
        this.reports = new ArrayList<DailyReport>();
    }
    public Reports(ArrayList<DailyReport> reports) {
        this.reports = reports;
    }

    public ArrayList<DailyReport> getReports() {
        return reports;
    }
    public void setReports(ArrayList<DailyReport> reports) {
        this.reports = reports;
    }

    public void addReport(DailyReport r){
        for(int i=0; i<reports.size(); i++) {
            if(r.getDate().toDateString().equals(reports.get(i).getDate().toDateString()))
                reports.add(i, r);
        }
        reports.add(r);
    }
    public void deleteReport(DailyReport r){
        reports.remove(r);
    }
    public DailyReport getReport(MyDate date){
        for(DailyReport r : reports) {
            if(r.getDate().toDateString().equals(date.toDateString()))
                return r;
        }
        return null;
    }
}