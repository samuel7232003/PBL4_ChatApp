package com.example.Client_ChatApp.controller;

public class StartEverything {
    private static SocketController socketController;

    public StartEverything(){
        socketController = new SocketController();
    }
    public static SocketController getSocketController(){return  socketController;}
}
