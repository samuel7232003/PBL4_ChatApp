package com.example.server_chatapp.DAO;

import java.sql.*;
import java.util.ArrayList;

import com.example.server_chatapp.controller.RoomController;
import com.example.server_chatapp.model.Room;

public class RoomDAO {
    public static ArrayList<Room> getAll(){
        try {
            Connection conn = connectMySQL.connectSQL();
            ArrayList<Room> rooms = new ArrayList<Room>();
            String sql = "SELECT * FROM room;";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                String roomId = resultSet.getString("ID_room");
                String roomName = resultSet.getString("Name_room");
                int clientNum = Integer.parseInt(resultSet.getString("ClientNum"));
                String roomType = resultSet.getString("Type");
                Room room = new Room(roomId, roomName, clientNum, roomType);
                rooms.add(room);
            }
            return rooms;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static Room CreateRoom(Room room){
        try{
            Connection conn = connectMySQL.connectSQL();
            // search ID last
            String sql_selectLastObj = "SELECT ID_room FROM room ORDER BY ID_room DESC LIMIT 1";
            Statement statement1 = conn.createStatement();
            ResultSet resultSet = statement1.executeQuery(sql_selectLastObj);

            String ID_room="";
            if(resultSet == null) ID_room = "RO01";
            else {
                while (resultSet.next()) ID_room = resultSet.getString("ID_room");
                ID_room = RoomController.createIdRoom(ID_room);
            }
            room.setID_room(ID_room);

            // insert vào room
            String sql = "INSERT INTO room(ID_room, Name_room, ClientNum, Type) VALUES ('" + room.getID_room() + "'" +
                                                                                      ",'" + room.getRoomName() + "'" +
                                                                                      ",'" + room.getClientNum() + "'" +
                                                                                      ",'" + room.getRoomType() + "')";
            Statement statement2 = conn.createStatement();
            statement2.executeUpdate(sql);
            return room;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static ArrayList<Room> searchRoom(ArrayList<String> roomIDs){
        try {
            Connection conn = connectMySQL.connectSQL();
            ArrayList<Room> rooms = new ArrayList<Room>();
            for(String roomID : roomIDs) {
                String sql = "SELECT * FROM room WHERE ID_room = '" + roomID + "';";
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                while(resultSet.next()){
                    String roomId = resultSet.getString("ID_room");
                    String roomName = resultSet.getString("Name_room");
                    int clientNum = Integer.parseInt(resultSet.getString("ClientNum"));
                    String roomType = resultSet.getString("Type");
                    Room room = new Room(roomID, roomName, clientNum, roomType);
                    rooms.add(room);
                }
            }
            return rooms;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
