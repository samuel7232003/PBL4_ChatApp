package com.example.Client_ChatApp.controller;

import com.example.Client_ChatApp.index;
import com.example.Client_ChatApp.model.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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
    private Label labletxt;
    @FXML
    private Label nameus1;
    @FXML
    private Label nameus2;
    @FXML
    private Label nameus3;
    @FXML
    private Label nameus4;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        us1.setVisible(false);
        us2.setVisible(false);
        us3.setVisible(false);
        us4.setVisible(false);
        titlep.setVisible(false);
        footerp.setVisible(false);
        int i = 1;
        for(Client client : StartEverything.getSocketController().getConnectedServer().getClients()){
            if(i==1){
                us1.setVisible(true);
                nameus1.setText(client.getName());
            }
            else if(i==2){
                us2.setVisible(true);
                nameus2.setText(client.getName());
            }
            else if(i==3){
                us3.setVisible(true);
                nameus3.setText(client.getName());
            }
            else if(i==3){
                us4.setVisible(true);
                nameus4.setText(client.getName());
            }
            i++;
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
            System.out.println(nameus1.getText());
        }
        else if(mouseEvent.getSource().equals(us2)){
            System.out.println(nameus2);
        }
        else if (mouseEvent.getSource().equals(us3)) {
            System.out.println(nameus3);
        }
        else if (mouseEvent.getSource().equals(us4)) {
            System.out.println(nameus4);
        }
    }
}
