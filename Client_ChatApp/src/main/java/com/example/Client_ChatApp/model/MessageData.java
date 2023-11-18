package com.example.Client_ChatApp.model;

import java.time.LocalTime;

public class MessageData {
    private int idMessage = 1;
    private String id_user;
    private String content;
    private LocalTime send_time;

    public MessageData(String id_user, String content, LocalTime send_time) {
        this.id_user = id_user;
        this.content = content;
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
    public LocalTime getSend_time(){
        return send_time;
    }
    public void setContent(String content) {
        this.content = content;
    }

}
