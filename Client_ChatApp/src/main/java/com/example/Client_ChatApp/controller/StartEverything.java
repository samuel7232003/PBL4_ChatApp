package com.example.Client_ChatApp.controller;

public class StartEverything {
    private static SocketController socketController;
    private static HomeController homeController;
    public StartEverything(){
        socketController = new SocketController();
        homeController = new HomeController();
    }
    public static SocketController getSocketController(){return  socketController;}
    public static HomeController getHomeController(){return homeController;}
}
