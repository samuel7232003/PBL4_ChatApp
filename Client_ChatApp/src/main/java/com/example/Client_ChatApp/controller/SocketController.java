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
        // loadHome();
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

                                for(Client client1 : connectedServer.getClients()){
                                    if(client1.getId().equals(Id_user)){
                                        connectedServer.removeClient(client1);
                                        break;
                                    }

                                }
                                updateUserOnlineList();
//                            for (Room room : allRooms) {
//                                if (room.users.contains(whoQuit)) {
//                                    Main.mainScreen.addNewMessage(room.id, "notify", whoQuit, "Đã thoát ứng dụng");
//                                    room.users.remove(whoQuit);
//                                }
//                            }
//                            Main.mainScreen.updateRoomUsersJList();

                                break;

                            }
                            case "new private room": {
                                int roomID = Integer.parseInt(bufferedReader.readLine());
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
            bufferedWriter.write("private"); // room type
            bufferedWriter.newLine();
            bufferedWriter.write(client.getId());
            bufferedWriter.newLine();
            bufferedWriter.write(id_user);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    //////////////// kick vô 1 user nào trên đó thì sẽ tự động vô hàm này
    public void SauClickVao1User(){
        String id_user=""; // id lấy được khi kick vào 1 user nào đó
        Room foundRoom = RoomController.findPrivateRoom(connectedServer.getRooms(), id_user);
        if(foundRoom == null){
            createPrivateRoom(id_user);
            // sau khi tạo th tự động đưa vào main chat với người đó
        }
        else{
            // đưa vaào màn hình chat với người đó
            updateRoomUsersJList();
        }
    }
    public void SauKhiNhanEnterChat(int roomId, String idSend, String content){
        LocalTime timenow = LocalTime.now();
        MessageData messageData = new MessageData(idSend, content,timenow);
        Room receiveRoom = RoomController.findRoom(connectedServer.getRooms(), roomId);
        receiveRoom.getMessageDatas().add(messageData);
    }

    public void updateRoomUsersJList() {
        System.out.println("updateRoomUsersJList");
        int id_room = 0; // id room khi được get
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
    public void loadHome() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(index.class.getResource("home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1150, 800);
        Stage stage = new Stage();
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();
    }
    ////////////////
//    public void SauClick(){
//        String id_user="";
//        Room foundRoom = RoomController.findPrivateRoom(connectedServer.getRooms(), id_user);
//        if(foundRoom == null){
//            createPrivateRoom(id_user);
//        }
//    }

}