package com.example.server_chatapp.controller;

import com.example.server_chatapp.DAO.ClientDAO;
import com.example.server_chatapp.model.Client;
import com.example.server_chatapp.model.Room;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HandlerController extends Thread{
    private Client client;
    private OutputStream osHandler;
    private InputStream isHandler;
    private Socket socketHandler;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    private ArrayList<Client> clientList;
    private int port;
    public Client getClient() {return client;}
    public HandlerController(Socket clientSocket){
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
        try{
            while (true){
                String header = bufferedReader.readLine();
                if(header == null) throw new IOException();

                System.out.println("Header: " + header);
                switch (header){
                    case "New login": {
                        String clientUsername = bufferedReader.readLine();
                        String clientPassword = bufferedReader.readLine();

                        String rs = ClientController.Login(clientUsername, clientPassword); // kiểm tra đăng nhập
                        // nếu mà k dính 2 cái trên thì dưới đó trả về id của client

                        if(rs.equals("Login-fail")) {
                            System.out.println("Đăng nhập không thành công");
                            bufferedWriter.write("Login-fail");
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                        }
                        else if(rs.equals("Password_fail")) {
                            System.out.println("Mật khẩu không chính xác");
                            bufferedWriter.write("Password_fail");
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                        }
                        else {
                            boolean logined = ClientController.checkClientIsLogin(rs);
                            if(logined) {
                                bufferedWriter.write("Account-logined");
                                bufferedWriter.newLine();
                                bufferedWriter.flush();
                                System.out.println("Tài khoản đã được đăng nhập!");
                            }
                            else {
                                this.client = ClientDAO.getClient(rs);

                                String name = this.client.getName();
                                System.out.println(name + " đăng nhập thành công");

                                SocketController.addHandlerClient(this);
                                //StartScreen.updateClient();

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
//                                    handlerController.getBufferedWriter().write("" + (SocketController.getClientSize() - 1));
//                                    handlerController.getBufferedWriter().newLine();
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
                    case "Sign up":{
                        String name = bufferedReader.readLine();
                        String username = bufferedReader.readLine();
                        String password = bufferedReader.readLine();
                        String email = bufferedReader.readLine();
                        System.out.println(name + username + password + email);

                        Client clientSignUp = new Client(name, username, password, email);
                        clientSignUp.setLogin(false);
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
//                                    handlerController.getBufferedWriter().write("" + (SocketController.getClientSize() - 1));
//                                    handlerController.getBufferedWriter().newLine();
                            handlerController.getBufferedWriter().write(this.client.getId());
                            handlerController.getBufferedWriter().newLine();
                            handlerController.getBufferedWriter().write(this.client.getName());
                            handlerController.getBufferedWriter().newLine();
                            handlerController.getBufferedWriter().flush();
                        }

                        break;
                    }
                    case "request create private room":{
                        String id_userFinal = bufferedReader.readLine();
                        String roomType = bufferedReader.readLine();
//                        int userCount = Integer.parseInt(bufferedReader.readLine());
//                        ArrayList<String> users = new ArrayList<String>();
//                        for (int i = 0; i < userCount; i++)
//                            users.add(bufferedReader.readLine());
                        // tìm kiếm user
                        HandlerController handlerController = null;
                        for(HandlerController handlerController1 : SocketController.getClientHandlers()){
                            if(handlerController1.getClient().getId().equals(id_userFinal)) {
                                handlerController = handlerController1;
                                break;
                            }
                        }

                        Room newRoom = new Room(roomType, this, handlerController);
                        SocketController.addRoom(newRoom);

                        handlerController.getBufferedWriter().write("new room");
                        handlerController.getBufferedWriter().newLine();
                        handlerController.getBufferedWriter().write(newRoom.getId());
                        handlerController.getBufferedWriter().newLine();
                        handlerController.getBufferedWriter().write(this.client.getId());
                        handlerController.getBufferedWriter().newLine();
                        handlerController.getBufferedWriter().write(roomType);
                        handlerController.getBufferedWriter().flush();


//                        for (int i = 0; i < userCount; i++) {
//                            BufferedWriter currentClientSender = Client.findClient(Main.socketController.connectedClient,
//                                    users.get(i)).sender;
//                            currentClientSender.write("new room");
//                            currentClientSender.newLine();
//                            currentClientSender.write("" + newRoom.id);
//                            currentClientSender.newLine();
//                            currentClientSender.write(thisClient.userName);
//                            currentClientSender.newLine();
//                            if (roomType.equals("private")) {
//                                // private chat thì tên room của mỗi người sẽ là tên của người kia
//                                currentClientSender.write(users.get(1 - i)); // user 0 thì gửi 1, user 1 thì gửi 0
//                                currentClientSender.newLine();
//                            } else {
//                                currentClientSender.write(roomName);
//                                currentClientSender.newLine();
//                            }
//                            currentClientSender.write(roomType);
//                            currentClientSender.newLine();
//                            currentClientSender.write("" + users.size());
//                            currentClientSender.newLine();
//                            for (String userr : users) {
//                                currentClientSender.write(userr);
//                                currentClientSender.newLine();
//                            }
//                            currentClientSender.flush();
//                        }
                        break;
                    }
                    case "Get id user":{
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
        }catch (IOException ex){
//            if(!StartScreen.checkConnectedUser() && client!=null){
//                try{
//                    for (HandlerController clientQuit : SocketController.getClientHandlers()) {
//                        if (!(clientQuit.getClient().getId()).equals(this.client.getId())) {
//                            clientQuit.getBufferedWriter().write("user quit");
//                            clientQuit.getBufferedWriter().newLine();
//                            clientQuit.getBufferedWriter().write(client.getId());
//                            clientQuit.getBufferedWriter().newLine();
//                            clientQuit.getBufferedWriter().write(client.getName());
//                            clientQuit.getBufferedWriter().newLine();
//                            clientQuit.getBufferedWriter().flush();
//                        }
//                    }
//
////                    for (Room room : Main.socketController.allRooms)
////                        room.users.remove(thisClient.userName);
//                    socketHandler.close();
//                    System.out.println(client.getName() + " đã rời khỏi đoạn chat");
//                }
//                catch (IOException e){
//                    e.printStackTrace();
//                }
//                SocketController.removeClient(this);
//                ClientController.Logout(client.getId());
//                StartScreen.updateClient();
//            }
        }
    }
}
