package com.example.server_chatapp.controller;

import com.example.server_chatapp.model.Client;
import com.example.server_chatapp.model.Room;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SocketController{
    private String serverName;
    private int serverPort;
    ServerSocket serverSocket;
    private static ArrayList<HandlerController> clients;
    private static ArrayList<Room> allRooms;



    public void setServerName(String serverName){this.serverName = serverName;}
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
    public static ArrayList<HandlerController> getClientHandlers() {
        return clients;
    }
    public static void addHandlerClient(HandlerController client){
        clients.add(client);
    }
    public static void removeClient(HandlerController client){
        clients.remove(client);
    }
    public static int getClientSize(){
        return clients.size();
    }
    public static void addRoom(Room room){
        allRooms.add(room);
    }
    public static void removeRoom(Room room){ allRooms.remove(room);}
    public static ArrayList<Client> getClientInfors(){
        ArrayList<Client> clientList= new ArrayList<Client>();
        for(HandlerController handlerController : clients){
            clientList.add(handlerController.getClient());
        }
        return clientList;
    }
    public SocketController(){
        clients = new ArrayList<HandlerController>();
        allRooms = new ArrayList<Room>();
    }
    public void OpenSocket(int port, TextArea tbtxt) {
        try {
            serverSocket = new ServerSocket(port);
            new Thread(() -> {
                try {
                    do {
                        tbtxt.setText("Waiting for client");

                        Socket clientSocket = serverSocket.accept();

                        HandlerController handlerController = new HandlerController(clientSocket);
                        handlerController.start();
                    } while (serverSocket != null && !serverSocket.isClosed());
                } catch (IOException e) {
                    tbtxt.appendText("Server or client socket closed");
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
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
    public static void updateClient(){
        System.out.println("Các tài khoản hiện đang online:");
        for(HandlerController handlerController : clients){
            System.out.println(handlerController.getClient().getName());
        }
    }
    public boolean checkSocketController(){return  serverSocket.isClosed();}
    public void CloseSocket() {
        try {
            for (HandlerController handlerController : clients) {
                ClientController.Logout(handlerController.getClient().getId());
                handlerController.getClient().getSocket().close();
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
