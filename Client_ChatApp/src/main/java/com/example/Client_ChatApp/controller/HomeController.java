package com.example.Client_ChatApp.controller;

import com.example.Client_ChatApp.index;
import com.example.Client_ChatApp.model.Client;
import com.example.Client_ChatApp.model.MessageData;
import com.example.Client_ChatApp.model.Room;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomeController implements Initializable{
    @FXML
    private Pane userOnl1;
    @FXML
    private Pane userOnl2;
    @FXML
    private Pane userOnl3;
    @FXML
    private Pane userOnl4;
    @FXML
    private Label nameus1;
    @FXML
    private Label nameus2;
    @FXML
    private Label nameus3;
    @FXML
    private Label nameus4;
    @FXML
    private Pane ro1;
    @FXML
    private Pane ro2;
    @FXML
    private Pane ro3;
    @FXML
    private Pane ro4;
    @FXML
    private Pane titlep;
    @FXML
    private Pane footerp;
    @FXML
    private VBox ChatList;
    @FXML
    private Label labletxt;
    @FXML
    private Label namero1;
    @FXML
    private Label namero2;
    @FXML
    private Label namero3;
    @FXML
    private Label namero4;
    @FXML
    private Label main_name;
    @FXML
    private TextField content;
    @FXML
    AnchorPane main;

    ArrayList<String> idRoomList;
    ArrayList<String> idUserList;
    static boolean start = false;
    static String mainNameRoom = "";
    static String mainIDRoom = "";
    public Client client;

    public void setMainIDRoom(String mainIDRoom) {
        HomeController.mainIDRoom = mainIDRoom;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client = StartEverything.getSocketController().getClient();
        main_name.setText(mainNameRoom);
        setup();
        //System.out.println(main.getScene());
        int i = 1;
        idRoomList = new ArrayList<String>();
        for(Room room : StartEverything.getSocketController().getConnectedServer().getRooms()){
            if(i==1) setupListRoom(room, ro1, namero1);
            else if(i==2) setupListRoom(room, ro2, namero2);
            else if(i==3)setupListRoom (room, ro3, namero3);
            else if(i==4)setupListRoom (room, ro4, namero4);
            i++;
        }
        i=1;
        idUserList = new ArrayList<String>();
        for(Client client1 : StartEverything.getSocketController().getConnectedServer().getClients()){
            if(i==1) setupListUser(client1, userOnl1, nameus1);
            else if(i==2) setupListUser(client1, userOnl2, nameus2);
            else if(i==3) setupListUser(client1, userOnl3, nameus3);
            else if(i==4) setupListUser(client1, userOnl4, nameus4);
            i++;
        }
        if(start){
            addMessage();
        }
    }
    public void setupListRoom(Room room, Pane ro, Label namero){
        ro.setVisible(true);
        namero.setText(findRoomName(room));
        idRoomList.add(room.getId());
    }
    public void setupListUser(Client client, Pane us, Label nameus){
        us.setVisible(true);
        nameus.setText(client.getName());
        idUserList.add(client.getId());
    }
    public void setup(){
        ro1.setVisible(false);
        ro2.setVisible(false);
        ro3.setVisible(false);
        ro4.setVisible(false);
        userOnl1.setVisible(false);
        userOnl2.setVisible(false);
        userOnl3.setVisible(false);
        userOnl4.setVisible(false);
        titlep.setVisible(start);
        footerp.setVisible(start);
        ChatList.setVisible(start);
        Stage stage1 = StartEverything.getSocketController().getStage();
        stage1.setTitle(client.getName());
    }

    public String findRoomName(Room room){
        if (room.getType().equals("private")){
            for(Client client1 : room.getClients()){
                if (!client1.getId().equals(client.getId())){
                    return client1.getName();
                }
            }
        }
        else return room.getName();
        return "loi";
    }

//    public static void reload_() throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(index.class.getResource("home.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 1150, 800);
//        scene.getStylesheets().add(index.class.getResource("home.css").toExternalForm());
//        stage.setTitle(StartEverything.getSocketController().getClient().getName());
//        stage.setScene(scene);
//    }
    public void reload() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(index.class.getResource("home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1150, 800);
        scene.getStylesheets().add(index.class.getResource("home.css").toExternalForm());
        Stage stage1 = StartEverything.getSocketController().getStage();
        stage1.setScene(scene);
    }
    public void openChatUser(MouseEvent e) throws IOException {
        if(e.getSource().equals(userOnl1)) openByUser(nameus1, 0);
        else if(e.getSource().equals(userOnl2)) openByUser(nameus2, 1);
        else if(e.getSource().equals(userOnl3)) openByUser(nameus3, 2);
        else if(e.getSource().equals(userOnl4)) openByUser(nameus4, 3);
    }
    public void openByUser(Label nameus, int index) throws IOException {
        start = true;
        mainNameRoom = nameus.getText();
        boolean check = StartEverything.getSocketController().selectUser(idUserList.get(index));
        if(check){
            mainIDRoom = RoomController.findPrivateRoom(StartEverything.getSocketController().getRoomList(), idUserList.get(index)).getId();
        }
    }

    public void openChat(MouseEvent mouseEvent) {
        if(mouseEvent.getSource().equals(ro1)) openByRoom(namero1, 0);
        else if(mouseEvent.getSource().equals(ro2)) openByRoom(namero2, 1);
        else if (mouseEvent.getSource().equals(ro3)) openByRoom(namero3, 2);
        else if (mouseEvent.getSource().equals(ro4)) openByRoom(namero4, 3);
    }
    public void openByRoom(Label namero,int index){
        start = true;
        mainNameRoom = namero.getText();
        mainIDRoom = idRoomList.get(index);
        System.out.println(mainIDRoom);
        StartEverything.getSocketController().selectRoom(mainIDRoom);
    }
    static ArrayList<MessageData> ListMessage;
    public void addMessage() {
        ListMessage = StartEverything.getSocketController().getMessageData(mainIDRoom);
        for (MessageData message : ListMessage) {
            String time = "(" + message.getSend_time().getHour() + ":" + message.getSend_time().getMinute() + ")";
            String name = StartEverything.getSocketController().getNameById(message.getId_user());
            if (name == null) {
                VBox vb = new VBox();
                vb.setAlignment(Pos.BOTTOM_RIGHT);
                vb.setId("vb");
                Label lb = new Label(message.getContent());
                lb.setId("BoxMessageMe");
                vb.getChildren().add(lb);
                Label lb1 = new Label(time);
                vb.getChildren().add(lb1);
                ChatList.getChildren().add(vb);
            } else {
                VBox vb = new VBox();
                vb.setAlignment(Pos.BOTTOM_LEFT);
                vb.setId("vb");
                Label lb = new Label(name + ": " + message.getContent());
                lb.setId("BoxMessage");
                vb.getChildren().add(lb);
                Label lb1 = new Label(time);
                vb.getChildren().add(lb1);
                ChatList.getChildren().add(vb);
            }
        }
    }

    public void send_(KeyEvent e) throws IOException {
        if(e.getCode() == KeyCode.ENTER){
            send();
            reload();
        }
    }

    public void send() {
        String contentChat = content.getText();
        System.out.println(contentChat);
        StartEverything.getSocketController().clickEnterChat(mainIDRoom, contentChat);
    }
}
