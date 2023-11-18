package com.example.server_chatapp.model;

import com.example.server_chatapp.controller.HandlerController;

import java.util.ArrayList;

public class Room {
    public static int currentRoomID = 1;

    private int id;
    private String roomType;
    private  String name; // nếu là nhắn tin 1-1 thì name là tên người nhận, multi client thì là tên nhóm
    private ArrayList<HandlerController> handlerclientList;

    public Room(String roomType, HandlerController client1, HandlerController client2) {
        handlerclientList = new ArrayList<HandlerController>();
        this.id = currentRoomID++;
        this.roomType = roomType;
        this.handlerclientList.add(client1);
        this.handlerclientList.add(client2);
    }

    public int getId() {
        return id;
    }

    public ArrayList<HandlerController> getHandlerclientList() {
        return handlerclientList;
    }

    public static Room findRoom(ArrayList<Room> roomList, int id) {
        for (Room room : roomList)
            if (room.id == id)
                return room;
        return null;
    }
}
