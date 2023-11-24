package com.example.server_chatapp.controller;

import com.example.server_chatapp.DAO.RoomDAO;
import com.example.server_chatapp.DAO.RoomDetailDAO;
import com.example.server_chatapp.model.Client;
import com.example.server_chatapp.model.Room;

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
        for (Room room : roomList)
            if (room.getID_room().equals(idRoom))
                return room;
        return null;
    }
    public static void searchExistedRoom(String id_user){
        ArrayList<String> roomIDs = RoomDetailDAO.returnMyRooms(id_user);
        ArrayList<Room> rooms = RoomDAO.searchRoom(roomIDs);
        ArrayList<String> idUserinRooms;
        ArrayList<Client> clientInRoom = new ArrayList<Client>();
        ArrayList<Client> clients = ClientController.getClients();
        for(Room room : rooms){
            idUserinRooms = RoomDetailDAO.returnIDuserExsitRoom(room.getID_room());
            int i = 0;
            for(String idUser : idUserinRooms){
                clientInRoom.add(ClientController.getClient(clients, idUser));
            }
            for(Client client:clientInRoom) System.out.println(client.getId() + ": " + client.getName());
            room.setClients(clientInRoom);
            clientInRoom.clear();
        }
        for(Room room : rooms){
            System.out.println(room.getID_room() + " có " + room.getClientNum() + " user:");
            for(Client client : room.getClients()) System.out.println(client.getId() + ": " + client.getName());
        }
        // chạy vòng lặp để lấy lên các handlerController để add vào rooms, trả về cho handlerController
    }

}
