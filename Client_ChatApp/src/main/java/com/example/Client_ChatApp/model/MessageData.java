package com.example.Client_ChatApp.model;

import java.sql.Time;

public class MessageData {
    private String send_id;
    private String type;
    private String content;
    private Time send_time;

    public MessageData(String send_id, String type, String content, Time send_time) {
        this.send_id = send_id;
        this.type = type;
        this.content = content;
        this.send_time = send_time;
    }

    public String getSend_id() {
        return send_id;
    }

    public void setSend_id(String send_id) {
        this.send_id = send_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Time getSend_time() {
        return send_time;
    }

    public void setSend_time(Time send_time) {
        this.send_time = send_time;
    }
}
