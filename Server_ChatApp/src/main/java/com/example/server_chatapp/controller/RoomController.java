package com.example.server_chatapp.controller;

import com.example.server_chatapp.DAO.RoomDAO;
import com.example.server_chatapp.DAO.RoomDetailDAO;
import com.example.server_chatapp.model.Room;

import java.util.ArrayList;

public class RoomController {
    public static Room createRoom(String nameRoom,ArrayList<String> clientId, String roomType){
        ArrayList<HandlerController> handlerControllers = new ArrayList<HandlerController>();
        for(String clientID : clientId){
            for(HandlerController handlerController : SocketController.getClientHandlers()){
                if(handlerController.getClient().getId().equals(clientID)){
                    handlerControllers.add(handlerController);
                    continue;
                }
            }
        }
        Room room = new Room(nameRoom, clientId.size(), roomType, handlerControllers);
        room = RoomDAO.CreateRoom(room);
        RoomDetailDAO.InsertRoomDetail(room);
        return room;
    }
    public static String createIdRoom(String ID_roomLast){
        int numRoom = Integer.parseInt(ID_roomLast.substring(2));
        String ID_newRoom = "RO";
        numRoom++;
        if(numRoom < 10) ID_newRoom += "0" + numRoom;
        else ID_newRoom += numRoom;
        return ID_newRoom;
    }
    public static Room findRoom(ArrayList<Room> roomList, String idRoom) {
        for (Room room : roomList)
            if (room.getID_room().equals(idRoom))
                return room;
        return null;
    }
}
