package com.murtaza.i180595_i180599;

import java.util.ArrayList;
import java.util.List;

public class Message {
    private String message, time, username, to, from;

    public Message() {}

    public Message(String message, String time, String username, String to, String from) {
        this.message = message;
        this.time = time;
        this.username = username;
        this.from = from;
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
