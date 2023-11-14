module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.Client_ChatApp to javafx.fxml;
    exports com.example.Client_ChatApp;

    opens com.example.Client_ChatApp.controller to javafx.fxml;
    exports com.example.Client_ChatApp.controller;
}