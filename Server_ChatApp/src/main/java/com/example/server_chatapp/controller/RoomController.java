package com.example.server_chatapp.controller;

import com.example.server_chatapp.DAO.RoomDAO;
import com.example.server_chatapp.DAO.RoomDetailDAO;
import com.example.server_chatapp.DAO.RoomMessageDAO;
import com.example.server_chatapp.model.Client;
import com.example.server_chatapp.model.Room;
import com.example.server_chatapp.model.RoomMessage;

import java.util.ArrayList;

public class RoomController {
    public static Room createRoom(String nameRoom,ArrayList<String> clientId, String roomType){
        ArrayList<Client> clients = new ArrayList<Client>();
        for(String clientID : clientId){
            for(HandlerController handlerController : SocketController.getClientHandlers()){
                if(handlerController.getClient().getId().equals(clientID)){
                    clients.add(handlerController.getClient());
                    continue;
                }
            }
        }
        Room room = new Room(nameRoom, clientId.size(), roomType, clients);
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
        for (Room room : roomList){
            if (room.getID_room().equals(idRoom)){
                return room;
            }
        }
        return null;
    }
    public static ArrayList<Room> getAllRoom(){
        ArrayList<Room> rooms = RoomDAO.getAll();
        ArrayList<String> idUserinRooms;
        ArrayList<Client> clients = ClientController.getAllClients();
        for(Room room : rooms){
            ArrayList<Client> clientInRoom = new ArrayList<Client>();
            idUserinRooms = RoomDetailDAO.returnIDuserExsitRoom(room.getID_room());
            int i = 0;
            for(String idUser : idUserinRooms){
                clientInRoom.add(ClientController.getClient(clients, idUser));
            }
            ArrayList<RoomMessage> roomMessages = RoomMessageDAO.getMessagebByIdRoom(room.getID_room());
            room.setClients(clientInRoom);
            room.setMessages(roomMessages);
        }
        return rooms;
    }
    public static ArrayList<Room> searchExistedRoom(String id_user){
        ArrayList<Room> rooms = SocketController.getAllRooms();
        ArrayList<Room> roomReturns = new ArrayList<Room>();
        for(Room room : rooms){
            for(Client client : room.getClients()){
                if(client.getId().equals(id_user)){
                    roomReturns.add(room);
                    continue;
                }
            }
        }
        return roomReturns;
    }
}
