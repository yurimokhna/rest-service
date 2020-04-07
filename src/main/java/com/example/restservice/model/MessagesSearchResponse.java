package com.example.restservice.model;

import java.util.ArrayList;

public class MessagesSearchResponse {
    private ArrayList<Message> messages;
    private int countMessage;
    private int page;

    public int getCountPage() {
        return countPage;
    }

    public void setCountPage(int countPage) {
        this.countPage = countPage;
    }

    int countPage;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCountMessage() {
        return countMessage;
    }

    public void setCountMessage(int countMessage) {
        this.countMessage = countMessage;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
