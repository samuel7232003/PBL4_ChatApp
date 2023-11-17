package com.example.Client_ChatApp.controller;

import com.example.Client_ChatApp.index;
import com.example.Client_ChatApp.model.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class SingupController {
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

    public void signup(ActionEvent event) throws IOException {
        String fullname = fullnametxt.getText().trim();
        String gmail = gmailtxt.getText().trim();
        String username = usernametxt.getText().trim();
        String pwd = pwdtxt.getText().trim();
        String repwd = repwdtxt.getText().trim();
        Client client = new Client(fullname, gmail, username, pwd);
//        String s = SocketController.singupSocket(client);
        String s = StartEverything.getSocketController().singupSocket(client);
        if (s == "Success") {
            Stage stage1 = (Stage)((Node) event.getSource()).getScene().getWindow();
            //SocketController.StartAll();
            //loadHome();
            System.out.println("Đăng kí thành công");
        }
        else if(s=="User name existed"){
            errortxt_.setText("Tài khoản đã tồn tại!");
            return;
        }
    }

    public void onLoginClick(MouseEvent mouseEvent) throws IOException {
        Stage stage1 = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
        stage1.close();
        FXMLLoader fxmlLoader = new FXMLLoader(index.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1150, 800);
        Stage stage = new Stage();
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
    public void loadHome(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SingupController.class.getResource("home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1150, 800);
        stage.setTitle("Home");
        stage.setScene(scene);
    }
}
