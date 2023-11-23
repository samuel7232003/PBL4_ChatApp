package com.example.server_chatapp.DAO;

import com.example.server_chatapp.controller.HandlerController;
import com.example.server_chatapp.controller.RoomController;
import com.example.server_chatapp.model.Client;
import com.example.server_chatapp.model.Room;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RoomDetailDAO {
    public static void InsertRoomDetail(Room room){
        try{
            Connection conn = connectMySQL.connectSQL();

            // insert vào room detail
            for(Client client : room.getClients()){
                String sql = "INSERT INTO room_detail(Id_room, Id_user) VALUES ('" + room.getID_room() + "','" + client.getId() + "')";
                Statement statement2 = conn.createStatement();
                statement2.executeUpdate(sql);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static ArrayList<String> returnMyRooms(String id_user){
        int n = 0;
        ArrayList<String> roomIDs = new ArrayList<String>();
        try {
            Connection conn = connectMySQL.connectSQL();
            String sql = "SELECT * FROM room_detail WHERE Id_user='" + id_user + "'";
            var statement = conn.prepareStatement(sql);
            var resultSet = statement.executeQuery();
            while (resultSet.next()) roomIDs.add(resultSet.getString("ID_room"));
            return roomIDs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static ArrayList<String> returnIDuserExsitRoom(String id_room){
        try{
            ArrayList<String> IdUserExisted = new ArrayList<String>();
            Connection conn = connectMySQL.connectSQL();
            String sql = "SELECT * FROM room_detail WHERE ID_room = '" + id_room + "'";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                IdUserExisted.add(resultSet.getString("Id_user"));
            }
            return IdUserExisted;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
