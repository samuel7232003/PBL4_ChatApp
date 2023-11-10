package com.example.server_chatapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class index extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(index.class.getResource("serverUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1150, 800);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
