package com.example.Client_ChatApp.controller;

import com.example.Client_ChatApp.index;
import com.example.Client_ChatApp.model.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomeController implements Initializable{
    @FXML
    private Pane us1;
    @FXML
    private Pane us2;
    @FXML
    private Pane us3;
    @FXML
    private Pane us4;
    @FXML
    private Pane titlep;
    @FXML
    private Pane footerp;
    @FXML
    private VBox ChatList;
    @FXML
    private Label labletxt;
    @FXML
    private Label nameus1;
    @FXML
    private Label nameus2;
    @FXML
    private Label nameus3;
    @FXML
    private Label nameus4;
    @FXML
    private Label main_name;
    @FXML
    private TextField content;
    public void load(MouseEvent mouseEvent) {
//        us1.setVisible(false);
//        us2.setVisible(false);
//        us3.setVisible(false);
//        us4.setVisible(false);
//        titlep.setVisible(false);
//        footerp.setVisible(false);
//        int num_us = connectedServer.getNum();
//        int i = 1;
////        System.out.println(num_us);
//        for(Client client : connectedServer.getClients()){
//            if(i==1){
//                us1.setVisible(true);
//                nameus1.setText(client.getName());
//            }
//            else if(i==2){
//                us2.setVisible(true);
//            }
//            else if(i==3){
//                us3.setVisible(true);
//            }
//            i++;
//        }
    }
    ArrayList<String> idClientList;
    static boolean start = false;
    static String mainName = "";
    static String mainID = "";
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        us1.setVisible(false);
        us2.setVisible(false);
        us3.setVisible(false);
        us4.setVisible(false);
        titlep.setVisible(start);
        footerp.setVisible(start);
        ChatList.setVisible(start);
        main_name.setText(mainName);
        int i = 1;
        idClientList = new ArrayList<String>();
        for(Client client : StartEverything.getSocketController().getConnectedServer().getClients()){
            if(i==1){
                us1.setVisible(true);
                nameus1.setText(client.getName());
                idClientList.add(client.getId());
            }
            else if(i==2){
                us2.setVisible(true);
                nameus2.setText(client.getName());
                idClientList.add(client.getId());
            }
            else if(i==3){
                us3.setVisible(true);
                nameus3.setText(client.getName());
                idClientList.add(client.getId());
            }
            else if(i==3){
                us4.setVisible(true);
                nameus4.setText(client.getName());
                idClientList.add(client.getId());
            }
            i++;
        }
        if(start){
            addMessage();
        }
    }
    public void reload(MouseEvent e) throws IOException {
        Stage stage1 = (Stage)((Node) e.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(index.class.getResource("home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1150, 800);
        stage1.setTitle("Home");
        stage1.setScene(scene);
    }

    public void openChat(MouseEvent mouseEvent) {
        if(mouseEvent.getSource().equals(us1)){
            start = true;
            mainName = nameus1.getText();
            mainID = idClientList.get(0);
            System.out.println(idClientList.get(0));
            StartEverything.getSocketController().selectUser(idClientList.get(0));
        }
        else if(mouseEvent.getSource().equals(us2)){
            start = true;
            mainName = nameus2.getText();
            mainID = idClientList.get(1);
            System.out.println(idClientList.get(1));
            StartEverything.getSocketController().selectUser(idClientList.get(1));

        }
        else if (mouseEvent.getSource().equals(us3)) {
            start = true;
            mainName = nameus3.getText();
            mainID = idClientList.get(2);
            System.out.println(idClientList.get(2));
            StartEverything.getSocketController().selectUser(idClientList.get(2));

        }
        else if (mouseEvent.getSource().equals(us4)) {
            start = true;
            mainName = nameus4.getText();
            mainID = idClientList.get(3);
            System.out.println(idClientList.get(3));
            StartEverything.getSocketController().selectUser(idClientList.get(3));
        }
    }
    public void addMessage(){
        if(ListMessage!=null){
            for(String message : ListMessage){
                Label lb = new Label(message);
                ChatList.getChildren().add(lb);
            }
        }
    }
    static ArrayList<String> ListMessage;
    public void send(MouseEvent mouseEvent) {
        int idroom = StartEverything.getSocketController().returnRoomId(mainID);
        String contentChat = content.getText();
        System.out.println(mainID+":");
        System.out.println(contentChat);
        StartEverything.getSocketController().clickEnterChat(idroom, contentChat);
    }
}
