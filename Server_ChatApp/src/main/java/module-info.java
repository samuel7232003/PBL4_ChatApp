module com.example.server_chatapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.server_chatapp to javafx.fxml;
    exports com.example.server_chatapp;
    exports com.example.server_chatapp.controller;
    opens com.example.server_chatapp.controller to javafx.fxml;
}