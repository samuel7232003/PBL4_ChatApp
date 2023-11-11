package com.example.server_chatapp.model;

import com.example.server_chatapp.controller.HandlerController;

import java.util.ArrayList;

public class Room {
    public static int currentRoomID = 1;
    private int id;
    private String roomType;
    private  String name; // nếu là nhắn tin 1-1 thì name là tên người nhận, multi client thì là tên nhóm
    private ArrayList<HandlerController> clients;
    public Room(String roomType, HandlerController client, HandlerController clientmain) {
        this.id = currentRoomID++;
        this.clients.add(client);
        this.clients.add(clientmain);
    }

    public int getId() {
        return id;
    }

}
