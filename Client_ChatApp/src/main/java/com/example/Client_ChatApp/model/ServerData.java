package com.example.Client_ChatApp.model;

import java.util.ArrayList;

public class ServerData {
    private String ip;
    private int port;
    private boolean isOpen;
    private int connectAccountCount;
    private ArrayList<Client> clients;
    private ArrayList<Room> rooms;
    private int num = 5;
    public ArrayList<Room> getRooms() {
        return rooms;
    }
    public void AddRoom(Room room){rooms.add(room);}
    public int getConnectAccountCount() {
        return connectAccountCount;
    }
    public void setConnectAccountCount(int connectAccountCount) {
        this.connectAccountCount = connectAccountCount;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }
    public void addClient(Client client){
        clients.add(client);
    }
    public int getNumClients(){
        num = clients.size();
        return clients.size();
    }
    public  void removeClient(Client client){
        clients.remove(client);
    }
    public void setOpen(boolean open) {
        isOpen = open;
    }
    public ServerData(String ip, int port){
        this.ip = ip;
        this.port = port;
        this.clients = new ArrayList<Client>();
        this.rooms = new ArrayList<Room>();
        this.isOpen = false;
        this.connectAccountCount = 0;
    }

    public int getNum() {
        return num;
    }

    public ServerData(String ip, int port, boolean isOpen, int connectAccountCount) {
        this.ip = ip;
        this.port = port;
        this.isOpen = isOpen;
        this.connectAccountCount = connectAccountCount;
    }

}
