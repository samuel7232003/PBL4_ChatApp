package com.example.Client_ChatApp.controller;

import com.example.Client_ChatApp.index;
import com.example.Client_ChatApp.model.Client;
import com.example.Client_ChatApp.model.MessageData;
import com.example.Client_ChatApp.model.Room;
import com.example.Client_ChatApp.model.ServerData;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class SocketController {
    private static Client client;
    private static ServerData connectedServer;
    private static Socket socket;
    private static BufferedWriter bufferedWriter;
    private static BufferedReader bufferedReader;
    private static Scanner sc = new Scanner(System.in);

    public ServerData getConnectedServer(){
        return connectedServer;
    }

    public SocketController() {
        this.client = new Client();
        String ipAddress = getThisIP();
        int port = 2119;
        connectedServer = new ServerData(ipAddress, port);
        try {
            socket = new Socket(ipAddress, port);
            InputStream is = socket.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            OutputStream os = socket.getOutputStream();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.exit(0);
            return;
        }
    }

    public void getOnlineUserss(){
        for(int i = 0; i < connectedServer.getConnectAccountCount(); i++ ){
            try {
                String id_user = bufferedReader.readLine();
                String NameUser = bufferedReader.readLine();
                Client clientVari = new Client(id_user, NameUser);
                connectedServer.addClient(clientVari);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void setClient(Client client) {
        this.client = client;
    }

    public static void updateUserOnlineList(){
        System.out.println("Số user đang online: " + connectedServer.getNumClients());
        int i = 1;
        for(Client client : connectedServer.getClients()){
            System.out.println(i++ + ". " + client.getName());
        }
    }

    public static String getThisIP() {
        String ip = "";
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("google.com", 80));
            ip = socket.getLocalAddress().getHostAddress();
            socket.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
        return ip;
    }

    public String singupSocket(Client client1){
        try {
            bufferedWriter.write("Sign up");
            bufferedWriter.newLine();
            bufferedWriter.write(client1.getName());
            bufferedWriter.newLine();
            bufferedWriter.write(client1.getUsername());
            bufferedWriter.newLine();
            bufferedWriter.write(client1.getPassword());
            bufferedWriter.newLine();
            bufferedWriter.write(client1.getEmail());
            bufferedWriter.newLine();
            bufferedWriter.flush();
            String SignUpResult = bufferedReader.readLine();
            if(SignUpResult.equals("User name existed")){
                System.out.println("User name đã tồn tại");
                return "User name existed";
            } else if (SignUpResult.equals("Sign up success")) {
                client = client1;
                System.out.println("Đăng kí thành công!");
                client.setId(bufferedReader.readLine());
                client.setName(bufferedReader.readLine());
                connectedServer.setOpen(true);
                connectedServer.setConnectAccountCount(Integer.parseInt(bufferedReader.readLine()));
                getOnlineUserss();
                updateUserOnlineList();
                StartAll();
                return "Success";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public String loginSocket(String username, String pwd){
        try{
            bufferedWriter.write("New login");
            bufferedWriter.newLine();
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.write(pwd);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            String loginResult = bufferedReader.readLine();
            System.out.println(loginResult);

            if(loginResult.equals("Login-fail")){
                return "Login-fail";
            }
            else if(loginResult.equals("Password_fail")){
                return "Password_fail";
            }
            else if(loginResult.equals("Account-logined")){
                return "Account-logined";
            }
            else if(loginResult.equals("Login success")){
                System.out.println("Đăng nhập thành công!");
                client.setId(bufferedReader.readLine());
                client.setName(bufferedReader.readLine());
                connectedServer.setOpen(true);
                connectedServer.setConnectAccountCount(Integer.parseInt(bufferedReader.readLine()));
                getOnlineUserss();
                updateUserOnlineList();
                StartAll();
                return "Login success";
            }
        }catch (IOException ex){

        }
        return null;
    }

    public void StartAll() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String header = bufferedReader.readLine();
                        System.out.println("Header: " + header);
                        if (header == null)
                            throw new IOException();
                        switch (header) {
                            case "new user online": {
                                String Id_user = bufferedReader.readLine();
                                String Name_user = bufferedReader.readLine();
                                Client clientVari = new Client(Id_user, Name_user);
                                connectedServer.addClient(clientVari);
                                updateUserOnlineList();
                                break;
                            }
                            case "user quit": {
                                String Id_user = bufferedReader.readLine();
                                String Name_user = bufferedReader.readLine();

                                System.out.println(Name_user + " đã rời khỏi cuộc trò chuyện");
                                Client clientQuit = new Client();
                                for(Client client1 : connectedServer.getClients()){
                                    if(client1.getId().equals(Id_user)){
                                        clientQuit = client1;
                                        connectedServer.removeClient(client1);
                                        break;
                                    }
                                }
                                connectedServer.removeClient(clientQuit);

//                                for (Room room : connectedServer.getRooms()) {
//                                    if (room.getClients().contains(clientQuit))) {
//                                        room.getClients().remove(clientQuit);
//                                    }
//                                }
                                updateUserOnlineList();
                                break;

                            }
                            case "new private room": {
                                String roomIDstr = bufferedReader.readLine();
                                System.out.println(roomIDstr);
                                int roomID = Integer.parseInt(roomIDstr);
                                String id_userRequest = bufferedReader.readLine();
                                String type = bufferedReader.readLine();
                                ArrayList<Client> clientlist = new ArrayList<Client>();
                                clientlist.add(client);
                                String name = "";
                                for(Client client1 : connectedServer.getClients()){
                                    if(client1.getId().equals(id_userRequest)){
                                        name = client1.getName();
                                        clientlist.add(client1);
                                        break;
                                    }
                                }
                                // tên đây tức là ngươi phía kia khi nhắn sẽ laf tên thawngf kia
                                Room newRoom = new Room(roomID, name, type, clientlist);
                                connectedServer.AddRoom(newRoom);
                                System.out.println("Mã phòng: " + roomID);
                                break;
                            }
                            case "text from user to room": {
                                String idUserSend = bufferedReader.readLine();
                                int roomID = Integer.parseInt(bufferedReader.readLine());
                                String content = "";
                                char c;
                                do {
                                    c = (char) bufferedReader.read();
                                    if (c != '\0')
                                        content += c;
                                } while (c != '\0');
                                System.out.println(idUserSend + ": " + content);
                                // Main.mainScreen.addNewMessage(roomID, "text", user, content);
                                LocalTime timenow = LocalTime.now();
                                MessageData messageData = new MessageData(idUserSend, content,timenow);
                                Room receiveRoom = RoomController.findRoom(connectedServer.getRooms(), roomID);
                                receiveRoom.getMessageDatas().add(messageData);
                                break;
                            }
                        }
                    }
                }catch (IOException e){
                    System.out.println("Server đã đóng, ứng dụng sẽ thoát ngay lập tức!");
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                System.exit(0);
            }
        }).start();
    }
    public void createPrivateRoom(String id_user) {
        try {
            bufferedWriter.write("request create private room");
            bufferedWriter.newLine();
            bufferedWriter.write(id_user); // room name
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    public void sendTextToRoom(int roomID, String content) {
        try {
            bufferedWriter.write("text to room");
            bufferedWriter.newLine();
            bufferedWriter.write("" + roomID);
            bufferedWriter.newLine();
            bufferedWriter.write(content);
            bufferedWriter.write('\0');
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //////////////// kick vô 1 user nào trên đó thì sẽ tự động vô hàm này
    public void selectUser(String id_user){
        Room foundRoom = RoomController.findPrivateRoom(connectedServer.getRooms(), id_user);
        if(foundRoom == null){
            createPrivateRoom(id_user);
        }
        else{
            updateRoomUsersJList(foundRoom.getId());
        }
    }
    public void clickEnterChat(int roomId, String content){
        Room receiveRoom = RoomController.findRoom(connectedServer.getRooms(), roomId);
        System.out.println(roomId + ": " + content);
        sendTextToRoom(receiveRoom.getId(), content);
    }
    public ArrayList<MessageData> getMessageData(int roomId){
        Room receiveRoom = RoomController.findRoom(connectedServer.getRooms(), roomId);
        return receiveRoom.getMessageDatas();
    }
    public void updateRoomUsersJList(int id_room) {
        System.out.println("updateRoomUsersJList");
        Room theChattingRoom = RoomController.findRoom(connectedServer.getRooms(), id_room);
        String name ="";
        if (theChattingRoom != null) {
            ArrayList<Client> clients = theChattingRoom.getClients();
            // update dữ liệu của các message trong đoạn chat ra ngoài màn hình
            for(MessageData messageData:theChattingRoom.getMessageDatas()){
                for(Client client1:clients){
                    if(messageData.getId_user().equals(client1.getId())){
                        name = client1.getName();
                        break;
                    }
                }
                System.out.println(name + ": " +messageData.getContent()); // sysout ra người gửi và content
            }
        }
    }
    public int returnRoomId(String idClient){
        for(Room room : connectedServer.getRooms()){
            for(Client client1 : room.getClients()){
                if(client1.getId().equals(idClient)){
                    return room.getId();
                }
            }
        }
        return 0;
    }

}