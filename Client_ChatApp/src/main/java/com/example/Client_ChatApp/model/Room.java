package com.example.Client_ChatApp.model;

import java.util.ArrayList;

public class Room {
    private String idRoom;
    private String name;
    private String type; //kiểu là chat 1 1 hay chat room
    private ArrayList<Client> clients;
    private ArrayList<MessageData> messageDatas;
    public Room(String idRoom, String name, String type, ArrayList<Client> clients) {
        this.idRoom = idRoom;
        this.name = name;
        this.type = type;
        this.clients = clients;
        this.messageDatas = new ArrayList<MessageData>();
    }
    public String getId() {
        return idRoom;
    }

    public void setId(String idRoom) {
        this.idRoom = idRoom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
    }

    public ArrayList<MessageData> getMessageDatas() {
        return messageDatas;
    }
    public void setMessageDatas(ArrayList<MessageData> messageDatas) {
        this.messageDatas = messageDatas;
    }
}
