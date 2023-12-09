package com.example.server_chatapp.controller;

import com.example.server_chatapp.DAO.ClientDAO;
import com.example.server_chatapp.model.Client;
import com.example.server_chatapp.model.Room;
import com.example.server_chatapp.model.RoomMessage;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HandlerController extends Thread {
    private Client client;
    private OutputStream osHandler;
    private InputStream isHandler;
    private Socket socketHandler;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private ArrayList<Client> clientList;
    private int port;

    public Client getClient() {
        return client;
    }

    public HandlerController(Socket clientSocket) {
        try {
            this.client = new Client();
            this.socketHandler = clientSocket;
            this.osHandler = this.socketHandler.getOutputStream();
            this.isHandler = this.socketHandler.getInputStream();
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.osHandler, StandardCharsets.UTF_8)); // viết riêng
            this.bufferedReader = new BufferedReader(new InputStreamReader(this.isHandler, StandardCharsets.UTF_8)); // viết riêng
            this.port = this.socketHandler.getPort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String header = bufferedReader.readLine();
                if (header == null) throw new IOException();

                System.out.println("Header: " + header);
                switch (header) {
                    case "New login": {
                        String clientUsername = bufferedReader.readLine();
                        String clientPassword = bufferedReader.readLine();

                        String rs = ClientController.Login(clientUsername, clientPassword); // kiểm tra đăng nhập
                        // nếu mà k dính 2 cái trên thì dưới đó trả về id của client

                        if (rs.equals("Login-fail")) {
                            System.out.println("Đăng nhập không thành công");
                            bufferedWriter.write("Login-fail");
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                        } else if (rs.equals("Password_fail")) {
                            System.out.println("Mật khẩu không chính xác");
                            bufferedWriter.write("Password_fail");
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                        } else {
                            boolean logined = ClientController.checkClientIsLogin(rs);
                            if (logined) {
                                bufferedWriter.write("Account-logined");
                                bufferedWriter.newLine();
                                bufferedWriter.flush();
                                System.out.println("Tài khoản đã được đăng nhập!");
                            } else {
                                this.client = ClientDAO.getClient(rs);

                                String name = this.client.getName();
                                System.out.println(name + " đăng nhập thành công");

                                SocketController.addHandlerClient(this);
                                SocketController.updateClient();

                                this.bufferedWriter.write("Login success");
                                this.bufferedWriter.newLine();
                                this.bufferedWriter.flush();

                                this.bufferedWriter.write(client.getId());
                                this.bufferedWriter.newLine();
                                this.bufferedWriter.write(client.getName());
                                this.bufferedWriter.newLine();
                                this.bufferedWriter.write(client.getEmail());
                                this.bufferedWriter.newLine();
                                this.bufferedWriter.write(client.getPassword());
                                this.bufferedWriter.newLine();
                                this.bufferedWriter.flush();

//                              Hiện số lượng người đang onl - 1 nghĩa là trừ thằng thằng mà đang nhắn
                                ArrayList<Client> clients = ClientController.getAllClients();
                                this.bufferedWriter.write("" + (clients.size() - 1));
                                this.bufferedWriter.newLine();
                                this.bufferedWriter.flush();
                                // cập nhật lại danh sách user đang online cho thằng this client
                                for (Client client1 : clients) {
                                    if ((client1.getId()).equals(this.client.getId()))
                                        continue;
                                    this.bufferedWriter.write(client1.getId());
                                    this.bufferedWriter.newLine();
                                    this.bufferedWriter.write(client1.getName());
                                    this.bufferedWriter.newLine();
                                    this.bufferedWriter.write(""+client1.isLogin());
                                    this.bufferedWriter.newLine();
                                    this.bufferedWriter.flush();
                                }


                                // gửi thông tin những cái room mà this client đang tham gia về cho chính nó:
                                ArrayList<Room> rooms = RoomController.searchExistedRoom(client.getId());
                                this.bufferedWriter.write("" + rooms.size());
                                this.bufferedWriter.newLine();
                                this.bufferedWriter.flush();

                                for(Room room : rooms){
                                    this.bufferedWriter.write(room.getID_room());
                                    this.bufferedWriter.newLine();
                                    this.bufferedWriter.write(room.getRoomName());
                                    this.bufferedWriter.newLine();
                                    this.bufferedWriter.write(room.getRoomType());
                                    this.bufferedWriter.newLine();
                                    this.bufferedWriter.write("" + room.getClientNum());
                                    this.bufferedWriter.newLine();
                                    this.bufferedWriter.flush();
                                    for(Client clientInRoom : room.getClients()){
                                        this.bufferedWriter.write(clientInRoom.getId());
                                        this.bufferedWriter.newLine();
                                        this.bufferedWriter.write(clientInRoom.getName());
                                        this.bufferedWriter.newLine();
                                    }
                                    this.bufferedWriter.flush();
                                    // gửi thông tin về những cái chat cho room
                                    this.bufferedWriter.write("" + room.getMessages().size());
                                    this.bufferedWriter.newLine();
                                    for(RoomMessage roomMessage : room.getMessages()){
                                        this.bufferedWriter.write("" + roomMessage.getMessageOrder());
                                        this.bufferedWriter.newLine();
                                        this.bufferedWriter.write(roomMessage.getId_userSend());
                                        this.bufferedWriter.newLine();
                                        this.bufferedWriter.write(roomMessage.getContent());
                                        this.bufferedWriter.newLine();
                                        this.bufferedWriter.write(roomMessage.getMessType());
                                        this.bufferedWriter.newLine();
                                        this.bufferedWriter.write("" + roomMessage.getTimeSend());
                                        this.bufferedWriter.newLine();
                                    }
                                    this.bufferedWriter.flush();
                                }

                                //Gửi thông tin từ this client về các client khác
                                for (HandlerController handlerController : SocketController.getClientHandlers()) {

                                    if ((handlerController.getClient().getId()).equals(this.client.getId()))
                                        continue;
                                    handlerController.getBufferedWriter().write("new user online");
                                    handlerController.getBufferedWriter().newLine();
                                    handlerController.getBufferedWriter().write(this.client.getId());
                                    handlerController.getBufferedWriter().newLine();
                                    handlerController.getBufferedWriter().write(this.client.getName());
                                    handlerController.getBufferedWriter().newLine();
                                    handlerController.getBufferedWriter().flush();
                                }
                            }
                        }
                        break;
                    }
                    case "Sign up": {
                        String name = bufferedReader.readLine();
                        String username = bufferedReader.readLine();
                        String password = bufferedReader.readLine();
                        String email = bufferedReader.readLine();

                        Client clientSignUp = new Client(name, username, password, email);
                        clientSignUp.setLogin(true);
                        String result = ClientController.SignUp(clientSignUp);

                        String rs = ClientController.Login(username, password); // kiểm tra đăng nhập
                        this.client = ClientDAO.getClient(rs);


                        bufferedWriter.write(result);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();

                        SocketController.addHandlerClient(this);

                        this.bufferedWriter.write(client.getId());
                        this.bufferedWriter.newLine();
                        this.bufferedWriter.write(client.getName());
                        this.bufferedWriter.newLine();
                        this.bufferedWriter.flush();

//                            Hiện số lượng người đang onl - 1 nghĩa là trừ thằng thằng mà đang nhắn
                        this.bufferedWriter.write("" + (SocketController.getClientSize() - 1));
                        this.bufferedWriter.newLine();
                        this.bufferedWriter.flush();
                        // cập nhật lại danh sách user đang online cho thằng this client
                        for (HandlerController handlerController : SocketController.getClientHandlers()) {
                            if ((handlerController.getClient().getId()).equals(this.client.getId()))
                                continue;
                            this.bufferedWriter.write(handlerController.getClient().getId());
                            this.bufferedWriter.newLine();
                            this.bufferedWriter.write(handlerController.getClient().getName());
                            this.bufferedWriter.newLine();
                            this.bufferedWriter.flush();
                        }
//                              //Gửi thông tin từ this client về các client khác
                        for (HandlerController handlerController : SocketController.getClientHandlers()) {

                            if ((handlerController.getClient().getId()).equals(this.client.getId()))
                                continue;
                            handlerController.getBufferedWriter().write("new user online");
                            handlerController.getBufferedWriter().newLine();
                            handlerController.getBufferedWriter().write(this.client.getId());
                            handlerController.getBufferedWriter().newLine();
                            handlerController.getBufferedWriter().write(this.client.getName());
                            handlerController.getBufferedWriter().newLine();
                            handlerController.getBufferedWriter().flush();
                        }

                        break;
                    }
                    case "request edit my infor":{
                        String nameUser = bufferedReader.readLine();
                        String email = bufferedReader.readLine();
                        String password = bufferedReader.readLine();

                    }
                    case "request create room": {
                        String roomName = bufferedReader.readLine();
                        String roomType = bufferedReader.readLine();
                        int userCount = Integer.parseInt(bufferedReader.readLine());

                        ArrayList<String> clientId = new ArrayList<String>();

                        for (int i = 0; i < userCount; i++) clientId.add(bufferedReader.readLine());
                        // viết hàm tìm kiếm trong csdl đã có room hay chưa, nếu đã cos room thì show ra như cũ

                        Room newRoom = RoomController.createRoom(roomName, clientId, roomType);
                        SocketController.addRoom(newRoom);

                        int i = 0;
                        // gửi về cho các client
                        for (Client client1 : newRoom.getClients()) {
                            HandlerController handlerControllerFinal = SocketController.getHandlerClient(client1.getId());
                            if(handlerControllerFinal != null){
                                handlerControllerFinal.getBufferedWriter().write("new room");
                                handlerControllerFinal.getBufferedWriter().newLine();
                                handlerControllerFinal.getBufferedWriter().write(newRoom.getID_room());
                                handlerControllerFinal.getBufferedWriter().newLine();
                                handlerControllerFinal.getBufferedWriter().write(newRoom.getRoomType());
                                handlerControllerFinal.getBufferedWriter().newLine();
                                if (newRoom.getRoomType().equals("private")){
                                    handlerControllerFinal.getBufferedWriter().write(clientId.get(1 - i));
                                    handlerControllerFinal.getBufferedWriter().newLine();
                                }
                                else {
                                    handlerControllerFinal.getBufferedWriter().write(newRoom.getRoomName());
                                    handlerControllerFinal.getBufferedWriter().newLine();
                                }

                                handlerControllerFinal.getBufferedWriter().write("" + userCount);
                                handlerControllerFinal.getBufferedWriter().newLine();

                                for (Client clientOnRoom : newRoom.getClients()) {
                                    handlerControllerFinal.getBufferedWriter().write(clientOnRoom.getId());
                                    handlerControllerFinal.getBufferedWriter().newLine();
                                }

                                handlerControllerFinal.getBufferedWriter().flush();
                            }
                        }
                        break;
                    }
                    case "request add to room": {
                        String idRoom = bufferedReader.readLine();
                        int cnt = Integer.parseInt(bufferedReader.readLine());

                        ArrayList<String> clientId = new ArrayList<String>();

                        for (int i = 0; i < cnt; i++) clientId.add(bufferedReader.readLine());

                        // íeeets hàm để gửi về add đứa mới
                        Room room = RoomController.findRoom(SocketController.getAllRooms(), idRoom);
                        room.plusClientNum(cnt);
                        ArrayList<Client> clientOld = room.getClients();

                        ArrayList<Client> clientNew = ClientController.getClietsById(clientId);
                        for(Client client1 : clientNew)   room.getClients().add(client1);
                        RoomController.addUserToRoom(room, clientNew);
                        // gửi thông tin về các client cũ là có thêm các client mới vo
                        for(Client client1 : clientOld){
                            HandlerController handlerControllerFinal = SocketController.getHandlerClient(client1.getId());
                            if(handlerControllerFinal != null){
                                handlerControllerFinal.getBufferedWriter().write("new user to room");
                                handlerControllerFinal.getBufferedWriter().newLine();
                                handlerControllerFinal.getBufferedWriter().write(room.getID_room());
                                handlerControllerFinal.getBufferedWriter().newLine();
                                handlerControllerFinal.getBufferedWriter().write("" + clientNew.size());
                                handlerControllerFinal.getBufferedWriter().newLine();
                                for(Client client11 : clientNew){
                                    handlerControllerFinal.getBufferedWriter().write(client11.getId());
                                    handlerControllerFinal.getBufferedWriter().newLine();
                                }
                                handlerControllerFinal.getBufferedWriter().flush();
                            }
                        }
                        // gửi thông tin về các client mới là room chat mới
                        bufferedWriter.write("join to room");
                        bufferedWriter.newLine();
                        bufferedWriter.write(idRoom);
                        bufferedWriter.newLine();
                        bufferedWriter.write(room.getRoomName());
                        bufferedWriter.newLine();
                        bufferedWriter.write("" + (room.getClients().size()-1));
                        bufferedWriter.newLine();
                        // gửi thông tin tất cả client có trong room về
                        for(Client client1 : room.getClients()) {
                            if (!client1.getId().equals(client.getId())){
                                bufferedWriter.write(client1.getId());
                                bufferedWriter.newLine();
                            }
                        }
                        bufferedWriter.flush();

                        bufferedWriter.write("" + room.getMessages().size());
                        bufferedWriter.newLine();
                        for(RoomMessage roomMessage : room.getMessages()){
                            bufferedWriter.write(roomMessage.getId_userSend());
                            bufferedWriter.newLine();
                            bufferedWriter.write(roomMessage.getContent());
                            bufferedWriter.newLine();
                            bufferedWriter.write(roomMessage.getMessType());
                            bufferedWriter.newLine();
                            bufferedWriter.write("" + roomMessage.getMessageOrder());
                            bufferedWriter.newLine();
                            bufferedWriter.write("" + roomMessage.getTimeSend());
                            bufferedWriter.newLine();
                        }
                        bufferedWriter.flush();
                        break;
                    }
                    // gửi text
                    case "text to room": {
                        String roomID = bufferedReader.readLine();
                        String content = "";
                        char c;
                        do {
                            c = (char) bufferedReader.read();
                            if (c != '\0')
                                content += c;
                        } while (c != '\0');

                        System.out.println(roomID + ": " + content);

                        Room room = RoomController.findRoom(SocketController.getAllRooms(), roomID);
                        room.setMessageOrder();
                        RoomMessage roomMessage = new RoomMessage(roomID, this.client.getId(), room.getMessageOrder(), content, "text");
                        RoomMessageController roomMessageController = new RoomMessageController();
                        roomMessageController.insertMessage(roomMessage);
                        room.getMessages().add(roomMessage);
                        for (Client client1 : room.getClients()) {
                            HandlerController clientRecieve = SocketController.getHandlerClient(client1.getId());
                            if(clientRecieve != null){
                                System.out.println("Send text from " + client.getName() + " to " + client1.getName());
                                if (clientRecieve != null) {
                                    clientRecieve.getBufferedWriter().write("text from user to room");
                                    clientRecieve.getBufferedWriter().newLine();
                                    clientRecieve.getBufferedWriter().write(client.getId());
                                    clientRecieve.getBufferedWriter().newLine();
                                    clientRecieve.getBufferedWriter().write(roomID);
                                    clientRecieve.getBufferedWriter().newLine();
                                    clientRecieve.getBufferedWriter().write(content);
                                    clientRecieve.getBufferedWriter().write('\0');
                                    clientRecieve.getBufferedWriter().write("" + room.getMessageOrder());
                                    clientRecieve.getBufferedWriter().newLine();
                                    clientRecieve.getBufferedWriter().flush();
                                }
                            }
                        }

                        break;
                    }
                    case "file to room": {
                        String roomID = bufferedReader.readLine();
                        String fileName = bufferedReader.readLine();
                        int fileSize = Integer.parseInt(bufferedReader.readLine());

                        String pathSaveFile = "files/" + roomID;

                        File filesFolder = new File(pathSaveFile);
                        if (!filesFolder.exists())
                            filesFolder.mkdir();

                        String saveFileName = "files/" + roomID + "/" + fileName;

                        File file = new File(saveFileName);
                        byte[] buffer = new byte[1024];
                        InputStream in = socketHandler.getInputStream();
                        OutputStream out = new FileOutputStream(file);

                        int receivedSize = 0;
                        int count;
                        while ((count = in.read(buffer)) > 0) {
                            out.write(buffer, 0, count);
                            receivedSize += count;
                            if (receivedSize >= fileSize)
                                break;
                        }

                        out.close();
                        // gửi cho các client
                        Room room = RoomController.findRoom(SocketController.getAllRooms(), roomID);
                        room.setMessageOrder();
                        RoomMessage roomMessage = new RoomMessage(roomID, this.client.getId(), room.getMessageOrder(), file.getName(), "file");
                        RoomMessageController roomMessageController = new RoomMessageController();
                        roomMessageController.insertMessage(roomMessage);
                        room.getMessages().add(roomMessage);
                        for (Client client1 : room.getClients()) {
                            HandlerController clientRecieve = SocketController.getHandlerClient(client1.getId());
                            if(clientRecieve != null){
                                if (clientRecieve != null) {
                                    clientRecieve.bufferedWriter.write("file from user to room");
                                    clientRecieve.bufferedWriter.newLine();
                                    clientRecieve.bufferedWriter.write(this.client.getId());
                                    clientRecieve.bufferedWriter.newLine();
                                    clientRecieve.bufferedWriter.write("" + roomID);
                                    clientRecieve.bufferedWriter.newLine();
                                    clientRecieve.bufferedWriter.write(file.getName());
                                    clientRecieve.bufferedWriter.newLine();
                                    clientRecieve.bufferedWriter.flush();
                                }
                            }
                        }
                        break;
                    }
                    case "request download file": {
                        try {
                            String roomID = bufferedReader.readLine();
                            int messageIndex = Integer.parseInt(bufferedReader.readLine());
                            String fileName = bufferedReader.readLine();

//                            int dotIndex = fileName.lastIndexOf('.');

//                            fileName = "files/" + fileName.substring(0, dotIndex)
//                                    + String.format("%02d%03d", roomID, messageIndex) + fileName.substring(dotIndex);
                            fileName = "files/" + roomID + "/" + fileName;
                            File file = new File(fileName);

                            bufferedWriter.write("response download file");
                            bufferedWriter.newLine();
                            bufferedWriter.write("" + file.length());
                            bufferedWriter.newLine();
                            bufferedWriter.flush();

                            byte[] buffer = new byte[1024];
                            InputStream in = new FileInputStream(file);
                            OutputStream out = socketHandler.getOutputStream();

                            int count;
                            while ((count = in.read(buffer)) > 0) {
                                out.write(buffer, 0, count);
                            }

                            in.close();
                            out.flush();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    }
                    case "audio to room": {
                        String roomID = bufferedReader.readLine();
                        int roomMessagesCount = Integer.parseInt(bufferedReader.readLine());
                        int audioDuration = Integer.parseInt(bufferedReader.readLine());
                        int audioByteSize = Integer.parseInt(bufferedReader.readLine());
                        System.out.println(roomID);
                        System.out.println(roomMessagesCount);
                        System.out.println(audioDuration);
                        System.out.println(audioByteSize);
                        File filesFolder = new File("files");
                        if (!filesFolder.exists())
                            filesFolder.mkdir();
                        String name ="audio" + roomID + roomMessagesCount;
                        String audioFileName = "files/" + name;

                        File file = new File(audioFileName);
                        byte[] buffer = new byte[1024];
                        InputStream in = socketHandler.getInputStream();
                        OutputStream out = new FileOutputStream(file);

                        int receivedSize = 0;
                        int count;
                        while ((count = in.read(buffer)) > 0) {
                            out.write(buffer, 0, count);
                            receivedSize += count;
                            if (receivedSize >= audioByteSize)
                                break;
                        }

                        out.close();
                        System.out.println("đã nhận xong");
                        Room room = RoomController.findRoom(SocketController.getAllRooms(), roomID);
                        room.setMessageOrder();
                        RoomMessage roomMessage = new RoomMessage(roomID, this.client.getId(), room.getMessageOrder(), file.getName(), "audio");
                        RoomMessageController roomMessageController = new RoomMessageController();
                        roomMessageController.insertMessage(roomMessage);
                        room.getMessages().add(roomMessage);
                        for (Client client1 : room.getClients()) {
                            HandlerController clientRecieve = SocketController.getHandlerClient(client1.getId());
                            if(clientRecieve != null){
                                clientRecieve.getBufferedWriter().write("audio from user to room");
                                clientRecieve.getBufferedWriter().newLine();
                                clientRecieve.getBufferedWriter().write(this.client.getId());
                                clientRecieve.getBufferedWriter().newLine();
                                clientRecieve.getBufferedWriter().write("" + roomID);
                                clientRecieve.getBufferedWriter().newLine();
                                clientRecieve.getBufferedWriter().write(name);
                                clientRecieve.getBufferedWriter().newLine();
                                clientRecieve.getBufferedWriter().write("" + audioDuration);
                                clientRecieve.getBufferedWriter().newLine();
                                clientRecieve.getBufferedWriter().flush();
                            }
                        }
                        break;
                    }
                    case "request edit room name" :{
                        String idRoom = bufferedReader.readLine();
                        String newRoomName = bufferedReader.readLine();

                        // lưu vaò db
                        Room room = RoomController.editRoomProfile(idRoom, newRoomName);
                        // gửi về các client
                        for (Client client1 : room.getClients()) {
                            HandlerController clientRecieve = SocketController.getHandlerClient(client1.getId());
                            if(clientRecieve != null){
                                if (clientRecieve != null) {
                                    clientRecieve.bufferedWriter.write("new name room");
                                    clientRecieve.bufferedWriter.newLine();
                                    clientRecieve.bufferedWriter.write(room.getID_room());
                                    clientRecieve.bufferedWriter.newLine();
                                    clientRecieve.bufferedWriter.write(room.getRoomName());
                                    clientRecieve.bufferedWriter.newLine();
                                    clientRecieve.bufferedWriter.write(this.client.getId());
                                    clientRecieve.bufferedWriter.newLine();
                                    clientRecieve.bufferedWriter.flush();
                                }
                            }
                        }
                        break;
                    }
                    case "request audio bytes": {
                        try {
                            String roomID = bufferedReader.readLine();
                            int messageIndex = Integer.parseInt(bufferedReader.readLine());
                            String name ="audio" + roomID + messageIndex;
                            String audioFileName = "files/" + name;
//                            String audioFileName = "files/audio" + String.format("%02d%03d", roomID, messageIndex);
                            File file = new File(audioFileName);

                            bufferedWriter.write("response audio bytes");
                            bufferedWriter.newLine();
                            bufferedWriter.write("" + file.length());
                            bufferedWriter.newLine();
                            bufferedWriter.flush();

                            byte[] buffer = new byte[1024];
                            InputStream in = new FileInputStream(file);
                            OutputStream out = socketHandler.getOutputStream();

                            int count;
                            while ((count = in.read(buffer)) > 0) {
                                out.write(buffer, 0, count);
                            }

                            in.close();
                            out.flush();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    }
                    case "Get id user": {
                        String id = client.getId();
                        bufferedWriter.write(id);
                        bufferedWriter.flush();
                    }

                    case "get name": {
                        bufferedWriter.write("Server của ĐT");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        break;
                    }
                }
            }
        } catch (IOException ex) {
            if (this.client != null) {// !SocketController.getSocketController() && viết lại
                try {
                    for (HandlerController clientQuit : SocketController.getClientHandlers()) {
                        if (!(clientQuit.getClient().getId()).equals(this.client.getId())) {
                            clientQuit.getBufferedWriter().write("user quit");
                            clientQuit.getBufferedWriter().newLine();
                            clientQuit.getBufferedWriter().write(this.client.getId());
                            clientQuit.getBufferedWriter().newLine();
                            clientQuit.getBufferedWriter().write(this.client.getName());
                            clientQuit.getBufferedWriter().newLine();
                            clientQuit.getBufferedWriter().flush();
                        }
                    }
                    socketHandler.close();
                    System.out.println(client.getName() + " đã rời khỏi đoạn chat");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SocketController.removeClient(this);
                ClientController.Logout(client.getId());
                SocketController.updateClient();
            }
        }
    }
}