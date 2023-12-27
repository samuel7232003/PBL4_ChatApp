package com.example.Client_ChatApp.controller;

import com.example.Client_ChatApp.index;
import com.example.Client_ChatApp.model.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
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
    @FXML
    private ImageView clientAva1;
    @FXML
    private ImageView clientAva;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Client> clients = new ArrayList<Client>(StartEverything.getSocketController().getConnectedServer().getClients());
        // for(Client client1 : clients) System.out.println(client1.getId());
        client = StartEverything.getSocketController().getClient();
        clientAva.setImage(getAvaImage(client.getId()));
        clientAva1.setImage(getAvaImage(client.getId()));
        namelb.setText(client.getName());
        emaillb.setText(client.getEmail());
        usernamelb.setText(client.getUsername());
        pwdlb.setText(client.getPassword());
    }

    public void openHomePage(MouseEvent mouseEvent) throws IOException {
        Stage stage1 = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(index.class.getResource("home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1150, 800);
        stage1.setScene(scene);
    }

    @FXML
    Image avaImage;

    public Image getAvaImage(String idUser){
        String prePath = avaImage.getUrl();
        String path = "";
        int i = 1;
        for(String e : prePath.split("/")){
            if(i<prePath.split("/").length){
                if(i==1) path=path+e;
                else path=path+"/"+e;
            }
            i++;
        }
        path = path + "/" + "ava" + idUser + ".jpg";
        Image image = new Image(path);
        if(image.isError()){
            return avaImage;
        }
        return image;
    }

    public void logout(MouseEvent event) throws IOException {
        StartEverything.getSocketController().getSocket().close();
    }

    public void requestEditProfile(){
        StartEverything.getSocketController().requestEditMyInfor(namelb.getText(), emaillb.getText(), pwdlb.getText());
    }
}
