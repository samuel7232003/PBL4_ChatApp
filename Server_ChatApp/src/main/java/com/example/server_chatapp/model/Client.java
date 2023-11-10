package com.example.server_chatapp.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

public class Client {
    private String id;
    private String name;
    private String username;
    private String password;
    private int Port;
    private String email;
    private  boolean isLogin;
    private Socket socket;
    public BufferedReader bufferedReader;
    public BufferedWriter bufferedWriter;

    public Client(String id, String name, String username, String password, String email, boolean isLogin) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        if (isLogin) this.isLogin = true;
        else this.isLogin = false;
    }

    public Client(String name, String username, String password, String email) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Client(){}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public int getPort() {
        return Port;
    }

    public void setPort(int port) {
        Port = port;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public void setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    public void setBufferedWriter(BufferedWriter bufferedWriter) {
        this.bufferedWriter = bufferedWriter;
    }
}
