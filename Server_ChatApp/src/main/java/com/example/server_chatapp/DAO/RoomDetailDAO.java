package com.example.server_chatapp.DAO;

import com.example.server_chatapp.controller.HandlerController;
import com.example.server_chatapp.controller.RoomController;
import com.example.server_chatapp.model.Room;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RoomDetailDAO {
    public static void InsertRoomDetail(Room room){
        try{
            Connection conn = connectMySQL.connectSQL();

            // insert vào room detail
            for(HandlerController handlerController : room.getClients()){
                String sql = "INSERT INTO room_detail(Id_room, Id_user) VALUES ('" + room.getID_room() + "','" + handlerController.getClient().getId() + "')";
                Statement statement2 = conn.createStatement();
                statement2.executeUpdate(sql);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
