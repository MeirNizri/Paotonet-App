package com.example.paotonet.Objects;

import java.util.Comparator;

public class Message_Comperator implements Comparator<Message> {

    @Override
    public int compare(Message o1, Message o2) {
        MyDate t1 = o1.getTime();
        MyDate t2 = o2.getTime();
        if(t1.getYear() != t2.getYear()) return t2.getYear()-t1.getYear();
        else if(t1.getMonth() != t2.getMonth()) return t2.getMonth()-t1.getMonth();
        else if(t1.getDay() != t2.getDay()) return t2.getDay()-t1.getDay();
        else if(t1.getHour() != t2.getHour()) return t2.getHour()-t1.getHour();
        else if(t1.getMinute() != t2.getMinute()) return t2.getMinute()-t1.getMinute();
        return t2.getSecond()-t1.getSecond();
    }
}
