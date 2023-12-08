package com.example.Client_ChatApp.controller;


import com.example.Client_ChatApp.index;
import com.example.Client_ChatApp.model.Client;
import com.example.Client_ChatApp.model.Room;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class RoomController {
    public static Room findRoom(ArrayList<Room> roomList, String idRoom) {
        for (Room room : roomList)
            if (room.getId().equals(idRoom))
                return room;
        return null;
    }
    public static Room findPrivateRoom(ArrayList<Room> roomList, String Id_OtherUser) {
        for (Room room : roomList) {
            if (room.getType().equals("private")){
                for (Client client : room.getClients()){
                    if(client.getId().equals(Id_OtherUser)){
                        return room;
                    }
                }
            }
        }
        return null;
    }
    public static Room findGroup(ArrayList<Room> roomList, String groupName) {
        for (Room room : roomList) {
            if (room.getType().equals("group") && room.getName().equals(groupName))
                return room;
        }
        return null;
    }

    public static ArrayList<Client> findOtherUserRoom(Room room, Client client ){
        ArrayList<Client> clients = new ArrayList<>();
        for (Client client1 : room.getClients()){
            if(!client1.getId().equals(client.getId())) clients.add(client1);
        }
        return clients;
    }

    public static ArrayList<Client> listUserRemoveListUser(ArrayList<Client> listA, ArrayList<Client> listB){
        ArrayList<Client> listC = new ArrayList<Client>(listA);
        for (Client clientA : listA){
            for (Client clientB : listB) if (clientA.getId().equals(clientB.getId())) listC.remove(clientA);
        }
        return listC;
    }

    public static String findRoomName(Room room){
        if (room.getType().equals("private")){
            for(Client client1 : room.getClients()){
                if (!client1.getId().equals(StartEverything.getSocketController().getClient().getId())){
                    return client1.getName();
                }
            }
        }
        else return room.getName();
        return "loi";
    }

}
