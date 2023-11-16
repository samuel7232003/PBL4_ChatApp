package com.example.Client_ChatApp.controller;


import com.example.Client_ChatApp.index;
import com.example.Client_ChatApp.model.Client;
import com.example.Client_ChatApp.model.Room;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class RoomController {
    public static Room findRoom(ArrayList<Room> roomList, int id) {
        for (Room room : roomList)
            if (room.getId() == id)
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
    public void loadHome() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(index.class.getResource("home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1150, 800);
        Stage stage = new Stage();
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();
    }
}
