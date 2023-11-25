package com.example.server_chatapp.model;

import com.example.server_chatapp.controller.HandlerController;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class RoomMessage {
    private String Id_room;
    private String Id_userSend;
    private int MessageOrder;
    private String Content;
    private LocalDateTime timeSend;

    public String getId_room() {
        return Id_room;
    }

    public void setId_room(String id_room) {
        Id_room = id_room;
    }

    public String getId_userSend() {
        return Id_userSend;
    }

    public void setId_userSend(String id_userSend) {
        Id_userSend = id_userSend;
    }

    public int getMessageOrder() {
        return MessageOrder;
    }

    public void setMessageOrder(int messageOrder) {
        MessageOrder = messageOrder;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public LocalDateTime getTimeSend() {
        return timeSend;
    }

    public void setTimeSend(LocalDateTime timeSend) {
        this.timeSend = timeSend;
    }

    public RoomMessage(String id_room, String id_userSend, int messageOrder, String content) {
        Id_room = id_room;
        Id_userSend = id_userSend;
        MessageOrder = messageOrder;
        Content = content;
        this.timeSend = LocalDateTime.now();
    }

    public RoomMessage(String id_room, String id_userSend, int messageOrder, String content, LocalDateTime timeSend) {
        Id_room = id_room;
        Id_userSend = id_userSend;
        MessageOrder = messageOrder;
        Content = content;
        this.timeSend = timeSend;
    }

    public RoomMessage(String id_room, String id_userSend, String content) {
        Id_room = id_room;
        Id_userSend = id_userSend;
        Content = content;
        this.timeSend = LocalDateTime.now();
    }
}
