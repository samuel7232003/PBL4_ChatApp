package com.example.server_chatapp.model;

import com.example.server_chatapp.controller.HandlerController;

import java.util.ArrayList;

public class Room {
    private String ID_room;
    private String roomName;
    private int clientNum;
    private String roomType;
    private ArrayList<Client> clients;
    private ArrayList<RoomMessage> messages;
    private int messageOrder = 0;
    public Room(String roomName, int clientNum, String roomType, ArrayList<Client> clients) {
        this.roomName = roomName;
        this.clientNum = clientNum;
        this.roomType = roomType;
        this.clients = clients;
    }

    public Room(String ID_room, String roomName, int clientNum, String roomType) {
        this.ID_room = ID_room;
        this.roomName = roomName;
        this.clientNum = clientNum;
        this.roomType = roomType;
    }

    public String getID_room() {
        return ID_room;
    }

    public void setID_room(String ID_room) {
        this.ID_room = ID_room;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getClientNum() {
        return clientNum;
    }

    public void setClientNum(int clientNum) {
        this.clientNum = clientNum;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
    }

    public ArrayList<RoomMessage> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<RoomMessage> messages) {
        this.messages = messages;
    }

    public int getMessageOrder() {
        return messageOrder;
    }

    public void setMessageOrder() {
        this.messageOrder++;
    }

}

