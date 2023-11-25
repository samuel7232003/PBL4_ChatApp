package com.example.server_chatapp.controller;

import com.example.server_chatapp.DAO.RoomMessageDAO;
import com.example.server_chatapp.model.RoomMessage;

public class RoomMessageController {
    public void insertMessage(RoomMessage roomMessage){
        RoomMessageDAO roomMessageDAO = new RoomMessageDAO();
        roomMessageDAO.insertMessageToDB(roomMessage);
    }
}
