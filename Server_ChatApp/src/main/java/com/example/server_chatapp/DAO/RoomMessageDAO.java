package com.example.server_chatapp.DAO;

import com.example.server_chatapp.model.Room;
import com.example.server_chatapp.model.RoomMessage;
import javafx.fxml.LoadException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RoomMessageDAO {
    public void insertMessageToDB(RoomMessage message){
        try{
            Connection conn = connectMySQL.connectSQL();
            String setTime = timeToString(message.getTimeSend());
            String sql = "INSERT INTO room_message(ID_room, ID_userSend, MessageOrder, Content, messageType, SendTime) VALUES ('" + message.getId_room() + "'" +
                                                                                                                ",'" + message.getId_userSend() + "'" +
                                                                                                                ",'" + message.getMessageOrder() + "'" +
                                                                                                                ",'" + message.getContent() + "'" +
                                                                                                                ",'" + message.getMessType() + "'" +
                                                                                                                ",'" + setTime + "')";
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }
    public static ArrayList<RoomMessage> getMessagebByIdRoom(String idRoom){
        ArrayList<RoomMessage> roomMessages = new ArrayList<>();
        try {
            Connection conn = connectMySQL.connectSQL();
            ArrayList<Room> rooms = new ArrayList<Room>();
            String sql = "SELECT * FROM room_message WHERE ID_room='" + idRoom + "'";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                String roomId = resultSet.getString("ID_room");
                String ID_userSend = resultSet.getString("ID_userSend");
                int messOrder = Integer.parseInt(resultSet.getString("MessageOrder"));
                String content = resultSet.getString("Content");
                String sendTime = resultSet.getString("SendTime");
                String messType = resultSet.getString("messageType");
                RoomMessage roomMessage = new RoomMessage(roomId, ID_userSend, messOrder,content,messType, timeToLocalDateTime(sendTime));
                roomMessages.add(roomMessage);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return roomMessages;
    }
    public String timeToString(LocalDateTime timenow){
        String datetime = timenow.toString();
        String timeSet = datetime.replace('T' , ' ').substring(0,19);
        return timeSet;
    }
    public static LocalDateTime timeToLocalDateTime(String timeSend){
        LocalDateTime timenow;
        timeSend = "2023-11-25 11:36:09";
        int year = Integer.parseInt(timeSend.substring(0,4));
        int month = Integer.parseInt(timeSend.substring(5,7));
        int day = Integer.parseInt(timeSend.substring(8,10));
        int hour = Integer.parseInt(timeSend.substring(11,13));
        int minute = Integer.parseInt(timeSend.substring(14,16));
        int second = Integer.parseInt(timeSend.substring(17,19));
        timenow = LocalDateTime.of(year,month,day,hour,minute,second);
        return timenow;
    }
}
