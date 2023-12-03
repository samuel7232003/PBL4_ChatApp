package com.example.Client_ChatApp.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class MessageData {
    private int messageOrder = 1;
    private String id_user;
    private String content;
    private String messType;
    private LocalDateTime send_time;

    public MessageData(int messageOrder, String id_user, String content, String messType, LocalDateTime send_time) {
        this.messageOrder = messageOrder;
        this.id_user = id_user;
        this.content = content;
        this.messType = messType;
        this.send_time = send_time;
    }

    public MessageData(String id_user, String content, String messType , LocalDateTime send_time) {
        this.id_user = id_user;
        this.content = content;
        this.messType = messType;
        this.send_time = send_time;
    }
    public String getId_user() {
        return id_user;
    }
    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
    public String getContent() {
        return content;
    }
    public LocalDateTime getSend_time(){
        return send_time;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getMessType(){ return messType;}

    public int getMessageOrder() {
        return messageOrder;
    }
}
