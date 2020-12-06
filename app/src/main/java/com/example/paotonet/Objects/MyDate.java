package com.example.paotonet.Objects;

import java.util.Calendar;

public class MyDate {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    public MyDate() {
        Calendar date = Calendar.getInstance();
        this.year = date.get(Calendar.YEAR);
        this.month = date.get(Calendar.MONTH)+1;
        this.day = date.get(Calendar.DAY_OF_MONTH);
        this.hour = date.get(Calendar.HOUR_OF_DAY);
        this.minute = date.get(Calendar.MINUTE);
        this.second = date.get(Calendar.SECOND);
    }
    public MyDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }
    public MyDate(int year, int month, int day, int hour, int minute, int second) {
        super();
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public int getHour() {
        return hour;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }
    public int getMinute() {
        return minute;
    }
    public void setMinute(int minute) {
        this.minute = minute;
    }
    public int getSecond() {
        return second;
    }
    public void setSecond(int second) {
        this.second = second;
    }
    public String toDateString() {
        return day+"\\"+month+"\\"+year;
    }
    public String toTimeString() {
        return String.format("%02d:%02d", hour,minute);
    }
    @Override
    public String toString() {
        return day+"\\"+month+"\\"+year + "  " + String.format("%02d:%02d:%02d",hour,minute,second);
    }
}
