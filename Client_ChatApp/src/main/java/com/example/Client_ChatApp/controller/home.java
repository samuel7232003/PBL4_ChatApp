package com.example.Client_ChatApp.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class home implements Initializable {
    @FXML
    private Pane us1;
    @FXML
    private Pane us2;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void an(Pane p){
        p.setVisible(false);
    }

    public void load(MouseEvent mouseEvent) {
    }
}
