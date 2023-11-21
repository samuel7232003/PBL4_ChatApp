package com.example.server_chatapp.model;

import com.example.server_chatapp.controller.HandlerController;

import java.sql.Time;
import java.time.LocalTime;

public class RoomMessage {
    private String Id_room;
    private String Id_userSend;
    private int MessageOrder;
    private String Content;
    private LocalTime timeSend;

    public RoomMessage(String id_room, String id_userSend, int messageOrder, String content, LocalTime timeSend) {
        Id_room = id_room;
        Id_userSend = id_userSend;
        MessageOrder = messageOrder;
        Content = content;
        this.timeSend = timeSend;
    }
}
