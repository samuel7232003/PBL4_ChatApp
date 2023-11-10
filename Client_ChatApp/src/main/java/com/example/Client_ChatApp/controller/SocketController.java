package com.example.Client_ChatApp.controller;

import com.example.Client_ChatApp.index;
import com.example.Client_ChatApp.model.Client;
import com.example.Client_ChatApp.model.ServerData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import java.util.Scanner;

public class SocketController implements Initializable {
    @FXML
    private TextField fullnametxt;
    @FXML
    private TextField gmailtxt;
    @FXML
    private TextField usernametxt;
    @FXML
    private PasswordField pwdtxt;
    @FXML
    private PasswordField repwdtxt;
    @FXML
    private Label errortxt_;
    @FXML
    private TextField usernametxtlo;
    @FXML
    private PasswordField pwdtxtlo;
    @FXML
    private Label errortxtlo_;



    Client client;
    ServerData connectedServer;
    Socket socket;
    BufferedWriter bufferedWriter;
    BufferedReader bufferedReader;
    Scanner sc = new Scanner(System.in);
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String ipAddress = getThisIP();
        int port = 2119;
        client = new Client();
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

    public void updateUserOnlineList(){
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

    public void signup(ActionEvent event) {
        String fullname = fullnametxt.getText().trim();
        String gmail = gmailtxt.getText().trim();
        String username = usernametxt.getText().trim();
        String pwd = pwdtxt.getText().trim();
        String repwd = repwdtxt.getId().trim();
        Client client1 = new Client(fullname,gmail,username,pwd);
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
                errortxt_.setText("User name đã tồn tại");
            } else if (SignUpResult.equals("Sign up success")) {
                System.out.println("Đăng kí thành công!");
                client.setId(bufferedReader.readLine());
                client.setName(bufferedReader.readLine());
                StartAll();
                Stage stage1 = (Stage)((Node) event.getSource()).getScene().getWindow();
                stage1.close();
                FXMLLoader fxmlLoader = new FXMLLoader(index.class.getResource("home.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 1150, 800);
                Stage stage = new Stage();
                stage.setTitle("Hello!");
                stage.setScene(scene);
                stage.show();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        StartAll();
    }

    public void onloginClick(MouseEvent mouseEvent) throws IOException {
        Stage stage1 = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
        stage1.close();
        FXMLLoader fxmlLoader = new FXMLLoader(index.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1150, 800);
        Stage stage = new Stage();
        stage.setTitle("Login!");
        stage.setScene(scene);
        stage.show();
    }

    public void login(ActionEvent event) {
        String username = usernametxtlo.getText().trim();
        String pwd = pwdtxtlo.getText().trim();
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
                errortxtlo_.setText("Đăng nhập không thành công");
                return;
            }
            else if(loginResult.equals("Password_fail")){
                errortxtlo_.setText("Mật khẩu không chính xác");
                return;
            }
            else if(loginResult.equals("Account-logined")){
                errortxtlo_.setText("Tài khoản đã được đăng nhập");
                return;
            }
            else if(loginResult.equals("Login success")){
                System.out.println("Đăng nhập thành công!");
                client.setId(bufferedReader.readLine());
                client.setName(bufferedReader.readLine());
                StartAll();
                Stage stage1 = (Stage)((Node) event.getSource()).getScene().getWindow();
                stage1.close();
                FXMLLoader fxmlLoader = new FXMLLoader(index.class.getResource("home.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 1150, 800);
                Stage stage = new Stage();
                stage.setTitle("Hello!");
                stage.setScene(scene);
                stage.show();
            }
        }catch (IOException ex){

        }
    }
    public void StartAll(){
        connectedServer.setOpen(true);
        try {
            connectedServer.setConnectAccountCount(Integer.parseInt(bufferedReader.readLine()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        updateUserOnlineList();
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
            bufferedWriter.write("request create room");
            bufferedWriter.newLine();
            bufferedWriter.write(id_user); // room name
            bufferedWriter.newLine();
            bufferedWriter.write("private"); // room type
            bufferedWriter.newLine();
//            bufferedWriter.write("2");
//            bufferedWriter.newLine();
            bufferedWriter.write(client.getId());
            bufferedWriter.newLine();
            bufferedWriter.write(id_user);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    public void onSignupClick(MouseEvent mouseEvent) throws IOException {
        Stage stage1 = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
        stage1.close();
        FXMLLoader fxmlLoader = new FXMLLoader(index.class.getResource("signup.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1150, 800);
        Stage stage = new Stage();
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}