package com.example.Client_ChatApp.controller;

import com.example.Client_ChatApp.model.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

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
            }
            else if(i==3){
                us3.setVisible(true);
            }
            i++;
        }
    }
}
