package com.example.server_chatapp.model;

import com.example.server_chatapp.controller.HandlerController;

import java.util.ArrayList;

public class Room {
    private String ID_room;
    private String roomName;
    private int clientNum;
    private String roomType;
    private ArrayList<HandlerController> clients;
    public Room(String roomName, int clientNum, String roomType, ArrayList<HandlerController> clients) {
        this.roomName = roomName;
        this.clientNum = clientNum;
        this.roomType = roomType;
        this.clients = clients;
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

    public ArrayList<HandlerController> getClients() {
        return clients;
    }

    public void setClients(ArrayList<HandlerController> clients) {
        this.clients = clients;
    }
}

