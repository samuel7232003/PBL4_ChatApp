package com.example.Client_ChatApp.controller;

import com.example.Client_ChatApp.index;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField usernametxtlo;
    @FXML
    private PasswordField pwdtxtlo;
    @FXML
    private Label errortxtlo_;
    StartEverything startEverything;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        SocketController socket = new SocketController();
        this.startEverything = new StartEverything();
    }
    public void login(ActionEvent event) throws IOException {
        // SocketController socket = new SocketController();
        String username = usernametxtlo.getText().trim();
        String pwd = pwdtxtlo.getText().trim();
        //String s = socket.loginSocket(username, pwd);
        String s = startEverything.getSocketController().loginSocket(username, pwd);
        if(s.equals("Login-fail")){
            errortxtlo_.setText("Tên đăng nhập không tồn tại!");
            return;
        }
        else if(s.equals("Password_fail")){
            errortxtlo_.setText("Sai mật khẩu!");
            return;
        }
        else if(s.equals("Account-logined")){
            errortxtlo_.setText("Tài khoản này đã được đăng nhập!");
            return;
        }
        else if(s.equals("Login success")){
            Stage stage1 = (Stage)((Node) event.getSource()).getScene().getWindow();
            loadHome(stage1);
        }
    }
    public void onSignupClick(MouseEvent mouseEvent) throws IOException {
        Stage stage1 = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
        stage1.close();
        FXMLLoader fxmlLoader = new FXMLLoader(index.class.getResource("signup.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1150, 800);
        Stage stage = new Stage();
        stage.setTitle("Sign up");
        stage.setScene(scene);
        stage.show();
    }
    public void loadHome(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(index.class.getResource("home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1150, 800);
        stage.setTitle("Home");
        stage.setScene(scene);
    }
}
