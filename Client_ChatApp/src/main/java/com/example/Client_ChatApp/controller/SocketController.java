package com.example.Client_ChatApp.controller;

import com.example.Client_ChatApp.index;
import com.example.Client_ChatApp.model.Client;
import com.example.Client_ChatApp.model.MessageData;
import com.example.Client_ChatApp.model.Room;
import com.example.Client_ChatApp.model.ServerData;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import static com.example.Client_ChatApp.controller.HomeController.getLastName;

public class SocketController {
    private static Client client;
    private static ServerData connectedServer;
    private static Socket socket;
    private static BufferedWriter bufferedWriter;
    private static BufferedReader bufferedReader;
    private static Stage stage;
    public Stage getStage(){
        return stage;
    }
    public ServerData getConnectedServer(){
        return connectedServer;
    }

    public SocketController() {
        this.client = new Client();
        String ipAddress = getThisIP();
        Stage stage1 = new Stage();
        stage = stage1;
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

    public void getAllUsers(){
        try {
            connectedServer.setConnectAccountCount(Integer.parseInt(bufferedReader.readLine()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(int i = 0; i < connectedServer.getConnectAccountCount(); i++ ){
            try {
                String id_user = bufferedReader.readLine();
                String NameUser = bufferedReader.readLine();
                boolean isLogin = Boolean.parseBoolean(bufferedReader.readLine());
                Client clientVari = new Client(id_user, NameUser, isLogin);
                connectedServer.addClient(clientVari);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void getRoomExisted(){
        try{
            int roomCount = Integer.parseInt(bufferedReader.readLine());
            for(int i = 0; i < roomCount ; i++){
                String idRoom = bufferedReader.readLine();
                String nameRoom = bufferedReader.readLine();
                String typeRoom = bufferedReader.readLine();
                int clientCount = Integer.parseInt(bufferedReader.readLine());
                ArrayList<Client> clients = new ArrayList<Client>();
                for(int j = 0; j <clientCount; j++){
                    Client clientInRoom = new Client();
                    clientInRoom.setId(bufferedReader.readLine());
                    clientInRoom.setName(bufferedReader.readLine());
                    clients.add(clientInRoom);
                }
                int roomMessageCount = Integer.parseInt(bufferedReader.readLine());
                ArrayList<MessageData> messageDatas = new ArrayList<MessageData>();
                for(int j = 0; j < roomMessageCount; j++){
                    int messageOrder = Integer.parseInt(bufferedReader.readLine());
                    String idUserSend = bufferedReader.readLine();
                    String content = bufferedReader.readLine();
                    String messType = bufferedReader.readLine();
                    LocalDateTime timeSend = LocalDateTime.parse(bufferedReader.readLine());
                    MessageData messageData = new MessageData(messageOrder, idUserSend, content, messType, timeSend);
                    messageDatas.add(messageData);
                }
                Room room = new Room(idRoom, nameRoom, typeRoom, clients,messageDatas);
                connectedServer.AddRoom(room);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void updateUserOnlineList(){
        System.out.println("Số user đang online: " + connectedServer.getClients().size());
        int i = 1;
        for(Client client : connectedServer.getClients()){
            if(client.isStatus()) System.out.println(i++ + ". " + client.getName());
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
                getAllUsers();
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
                getAllUsers();
                getRoomExisted();
                updateUserOnlineList();
                showRoom();
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
                                boolean check = false;
                                int i = 0;
                                for(Client client1 : connectedServer.getClients()){
                                    if(client1.getId().equals(Id_user)){
                                        check = true;
                                        connectedServer.getClients().get(i).setStatus(true);
                                        break;
                                    }
                                    i++;
                                }
                                if(!check){
                                    Client clientVari = new Client(Id_user, Name_user,true);
                                    connectedServer.addClient(clientVari);
                                }
                                updateUserOnlineList();
                                reloadOnSocket();
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
                                reloadOnSocket();
                                break;

                            }

                            case "new room": {
                                String roomID = bufferedReader.readLine();
                                String roomType = bufferedReader.readLine();
                                String roomName = bufferedReader.readLine();

                                int roomUserCount = Integer.parseInt(bufferedReader.readLine());
                                ArrayList<String> userIDs = new ArrayList<String>();
                                for (int i = 0; i < roomUserCount; i++)
                                    userIDs.add(bufferedReader.readLine());

                                ArrayList<Client> clientlist = new ArrayList<Client>();
                                for(String userID : userIDs){
                                    for(Client client1 : connectedServer.getClients()){
                                        if(client1.getId().equals(userID)){
                                            clientlist.add(client1);
                                            continue;
                                        }
                                    }
                                }ArrayList<MessageData> messageDatas = new ArrayList<MessageData>();
                                Room newRoom = new Room(roomID, roomName, roomType, clientlist, messageDatas);
                                System.out.println("Room id: " + roomID);
                                connectedServer.AddRoom(newRoom);
                                StartEverything.getHomeController().setMainRoom(newRoom);
                                reloadOnSocket();
                                break;
                            }
                            case "text from user to room": {
                                String idUserSend = bufferedReader.readLine();
                                String roomID = bufferedReader.readLine();
                                String content = "";
                                char c;
                                do {
                                    c = (char) bufferedReader.read();
                                    if (c != '\0')
                                        content += c;
                                } while (c != '\0');
                                System.out.println(idUserSend + ": " + content);
                                LocalDateTime timenow = LocalDateTime.now();
                                MessageData messageData = new MessageData(idUserSend, content, "text",timenow);
                                Room receiveRoom = RoomController.findRoom(connectedServer.getRooms(), roomID);
                                receiveRoom.getMessageDatas().add(messageData);
                                reloadOnSocket();
                                break;
                            }
                            case "file from user to room": {
                                String userID = bufferedReader.readLine();
                                String roomID = bufferedReader.readLine();
                                String fileName = bufferedReader.readLine();
                                System.out.println("Recevie file " + fileName + " from " + userID + " to room " + roomID);
                                Room receiveRoom = RoomController.findRoom(connectedServer.getRooms(), roomID);
                                LocalDateTime timenow = LocalDateTime.now();
                                MessageData messageData = new MessageData(userID, fileName, "file", timenow);
                                receiveRoom.getMessageDatas().add(messageData);
                                Platform.runLater(() ->{
                                    try {
                                        StartEverything.getHomeController().reload();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
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
    public void reloadOnSocket(){
        Platform.runLater(() ->{
            try {
                StartEverything.getHomeController().reload();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public void createPrivateRoom(String id_userFinal) {
        try {
            bufferedWriter.write("request create room");
            bufferedWriter.newLine();
            bufferedWriter.write("");
            bufferedWriter.newLine();
            bufferedWriter.write("private");
            bufferedWriter.newLine();
            bufferedWriter.write("2");
            bufferedWriter.newLine();
            bufferedWriter.write(client.getId());
            bufferedWriter.newLine();
            bufferedWriter.write(id_userFinal);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void createGroup(ArrayList<Client> clientSelected){
        clientSelected.add(client);
        String nameRoomBase = "";
        for(Client client1 : clientSelected){
            String lastName = getLastName(client1.getName());
            nameRoomBase += lastName;
            nameRoomBase += ", ";
        }
        nameRoomBase = nameRoomBase.substring(0, nameRoomBase.length()-2); // xoá dấu "," cuối cùng
        try {
            bufferedWriter.write("request create room");
            bufferedWriter.newLine();
            bufferedWriter.write(nameRoomBase);
            bufferedWriter.newLine();
            bufferedWriter.write("group");
            bufferedWriter.newLine();
            bufferedWriter.write("" + clientSelected.size());
            bufferedWriter.newLine();
            for(Client client1 : clientSelected){
                bufferedWriter.write(client1.getId());
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void sendTextToRoom(String roomID, String content) {
        try {
            bufferedWriter.write("text to room");
            bufferedWriter.newLine();
            bufferedWriter.write(roomID);
            bufferedWriter.newLine();
            bufferedWriter.write(content);
            bufferedWriter.write('\0');
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendFileToRoom(String roomID, String fileName, String filePath) {
        try {
            System.out.println("Send file " + fileName + " to room " + roomID);

            File file = new File(filePath);
            Room room = RoomController.findRoom(connectedServer.getRooms(), roomID);

            bufferedWriter.write("file to room");
            bufferedWriter.newLine();
            bufferedWriter.write(roomID);
            bufferedWriter.newLine();
            bufferedWriter.write("" + room.getMessageDatas().size());
            bufferedWriter.newLine();
            bufferedWriter.write(fileName);
            bufferedWriter.newLine();
            bufferedWriter.write("" + file.length());
            bufferedWriter.newLine();
            bufferedWriter.flush();

            byte[] buffer = new byte[1024];
            InputStream in = new FileInputStream(file);
            OutputStream out = socket.getOutputStream();

            int count;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }

            in.close();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //////////////// kick vô 1 user nào trên đó thì sẽ tự động vô hàm này
    public boolean selectUser(String id_user){
        Room foundRoom = RoomController.findPrivateRoom(connectedServer.getRooms(), id_user);
        if(foundRoom == null){
            createPrivateRoom(id_user);
            return  false;
        }
        else{
            return true;
        }
    }

    public void selectRoom(String id_room){
        updateRoomUsersJList(id_room);
    }

    public void clickEnterChat(String roomId, String content){
        Room receiveRoom = RoomController.findRoom(connectedServer.getRooms(), roomId);
        System.out.println(roomId + ": " + content);
        sendTextToRoom(receiveRoom.getId(), content);
    }
    public ArrayList<MessageData> getMessageData(String roomId){
        Room receiveRoom = RoomController.findRoom(connectedServer.getRooms(), roomId);
        return receiveRoom.getMessageDatas();
    }
    public void updateRoomUsersJList(String id_room) {
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

    public Client getClient() {
        return client;
    }

    public String returnRoomId(String idClient){
        for(Room room : connectedServer.getRooms()){
            for(Client client1 : room.getClients()){
                if(client1.getId().equals(idClient)){
                    return room.getId();
                }
            }
        }
        return "";
    }
    public String getNameById(String iduser){
        for(Client client1 : connectedServer.getClients()){
            if(client1.getId().equals(iduser)) return  client1.getName();
        }
        return  null;
    }
    public void showRoom(){
        for(Room room : connectedServer.getRooms()){
            System.out.println(room.getId() + " có các user: ");
            for(Client client1 : room.getClients()){
                System.out.print(client1.getName() + " ");
            }
            System.out.println();
        }
    }
    public ArrayList<Room> getRoomList(){
        return connectedServer.getRooms();
    }
    public String getLastName(String name){
        String Lastname = "";
        for(String e : name.split(" ")) Lastname = e;
        return Lastname;
    }
}