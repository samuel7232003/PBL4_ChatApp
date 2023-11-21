package com.example.server_chatapp.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.example.server_chatapp.controller.RoomController;
import com.example.server_chatapp.model.Room;

public class RoomDAO {
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
}
