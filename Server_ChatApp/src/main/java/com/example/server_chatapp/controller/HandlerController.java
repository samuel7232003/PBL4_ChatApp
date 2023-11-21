package com.example.server_chatapp.controller;

import com.example.server_chatapp.DAO.ClientDAO;
import com.example.server_chatapp.model.Client;
import com.example.server_chatapp.model.Room;

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
                        //StartScreen.updateClient();

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
                    case "request create room": {
                        String roomName = bufferedReader.readLine();
                        String roomType = bufferedReader.readLine();
                        int userCount = Integer.parseInt(bufferedReader.readLine());

                        ArrayList<String> clientId = new ArrayList<String>();

                        for (int i = 0; i < userCount; i++) clientId.add(bufferedReader.readLine());

                        Room newRoom = RoomController.createRoom(roomName, clientId, roomType);
                        SocketController.addRoom(newRoom);

                        int i = 0;
                        // gửi về cho các client
                        for (HandlerController handlerControllerFinal : newRoom.getClients()) {
                            handlerControllerFinal.getBufferedWriter().write("new room");
                            handlerControllerFinal.getBufferedWriter().newLine();
                            handlerControllerFinal.getBufferedWriter().write(newRoom.getID_room());
                            handlerControllerFinal.getBufferedWriter().newLine();
                            handlerControllerFinal.getBufferedWriter().write("private");
                            handlerControllerFinal.getBufferedWriter().newLine();

                            if (newRoom.getRoomType().equals("private"))
                                handlerControllerFinal.getBufferedWriter().write(clientId.get(1 - i));
                            else handlerControllerFinal.getBufferedWriter().write(newRoom.getRoomName());
                            handlerControllerFinal.getBufferedWriter().newLine();

                            handlerControllerFinal.getBufferedWriter().write("" + userCount);
                            handlerControllerFinal.getBufferedWriter().newLine();

                            for (HandlerController handlerController : newRoom.getClients()) {
                                handlerControllerFinal.getBufferedWriter().write(handlerController.getClient().getId());
                                handlerControllerFinal.getBufferedWriter().newLine();
                            }

                            handlerControllerFinal.getBufferedWriter().flush();
                        }
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
                        for (HandlerController clientRecieve : room.getClients()) {
                            System.out.println("Send text from " + client.getName() + " to " + clientRecieve.getClient().getName());
                            if (clientRecieve != null) {
                                clientRecieve.getBufferedWriter().write("text from user to room");
                                clientRecieve.getBufferedWriter().newLine();
                                clientRecieve.getBufferedWriter().write(client.getId());
                                clientRecieve.getBufferedWriter().newLine();
                                clientRecieve.getBufferedWriter().write(roomID);
                                clientRecieve.getBufferedWriter().newLine();
                                clientRecieve.getBufferedWriter().write(content);
                                clientRecieve.getBufferedWriter().write('\0');
                                clientRecieve.getBufferedWriter().flush();
                            }
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
//                    case "get connected count": {
//                        bufferedWriter.write("" + StartScreen.getSizeClient());
//                        bufferedWriter.newLine();
//                        bufferedWriter.flush();
//                        break;
//                    }
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