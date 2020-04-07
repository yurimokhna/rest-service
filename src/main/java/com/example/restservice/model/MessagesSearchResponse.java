package com.example.restservice.model;

import java.util.ArrayList;

public class MessagesSearchResponse {
    private ArrayList<Message> messages;

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
