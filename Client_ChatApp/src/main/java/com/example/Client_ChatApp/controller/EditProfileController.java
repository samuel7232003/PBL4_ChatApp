package com.example.Client_ChatApp.controller;

import com.example.Client_ChatApp.model.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EditProfileController implements Initializable {
    private Client client;
    @FXML
    private TextField namelb;
    @FXML
    private TextField emaillb;
    @FXML
    private TextField usernamelb;
    @FXML
    private PasswordField pwdlb;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Client> clients = new ArrayList<Client>(StartEverything.getSocketController().getConnectedServer().getClients());
        for(Client client1 : clients) System.out.println(client1.getId());
        client = ClientController.getClientById(clients, StartEverything.getSocketController().getClient().getId());
//        System.out.println(StartEverything.getSocketController().getClient().getId());
//        namelb.setText(client.getName());
//        emaillb.setText(client.getEmail());
//        usernamelb.setText(client.getUsername());
//        pwdlb.setText(client.getPassword());
    }
}
