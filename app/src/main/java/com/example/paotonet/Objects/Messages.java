package com.example.paotonet.Objects;

import java.util.ArrayList;

public class Messages {
    ArrayList<Message> messages;

    public Messages() {
        this.messages = new ArrayList<Message>();
    }
    public Messages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message m){
        messages.add(m);
    }
    public void deleteMessage(Message m){
        messages.remove(m);
    }

    public ArrayList<Message> getMessagesBySender(String sender){
        ArrayList<Message> result = new ArrayList<Message>();
        for(Message m : messages) {
            if(m.getSender().equals(sender) && !m.getDestination().equals("broadcast")) {
                result.add(m);
            }
        }
        return result;
    }
    public ArrayList<Message> getMessagesByDest(String dest){
        ArrayList<Message> result = new ArrayList<Message>();
        for(Message m : messages) {
            if(m.getDestination().equals(dest)) {
                result.add(m);
            }
        }
        return result;
    }
}
