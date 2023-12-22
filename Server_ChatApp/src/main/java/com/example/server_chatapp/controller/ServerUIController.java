package com.example.server_chatapp.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class ServerUIController implements Initializable {
    private int port = 2119;
    private static SocketController socketController;
    boolean isSocketOpened = false;
    public static SocketController getSocketController() {
        return socketController;
    }
    @FXML
    private Label iptxt;
    @FXML
    private Label porttxt;
    @FXML
    private TextArea tbtxt;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String ip = SocketController.getThisIP();
        iptxt.setText("Địa chỉ ip: " + ip);
        porttxt.setText("Port      : " + port);
        tbtxt.appendText("Danh sách user đang online: ");

        socketController = new SocketController();
        if(!isSocketOpened){
            socketController.setServerName("Server này là của ĐT");
            socketController.setServerPort(port);

            socketController.OpenSocket(port, tbtxt);
            isSocketOpened = true;
        }
        else {
            socketController.CloseSocket();
            isSocketOpened = false;
        }
    }

    public void closeSocket(){
        socketController.CloseSocket();
    }
}
