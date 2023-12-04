package com.example.Client_ChatApp.controller;

import com.example.Client_ChatApp.index;
import com.example.Client_ChatApp.model.Client;
import com.example.Client_ChatApp.model.MessageData;
import com.example.Client_ChatApp.model.Room;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomeController implements Initializable{
    @FXML
    private Pane titlep;
    @FXML
    private Pane footerp;
    @FXML
    private VBox ChatList;
    @FXML
    private Label main_name;
    @FXML
    private TextField content;
    @FXML
    Pane addUserMenu;
    @FXML
    private HBox listUserOnlHBox;
    @FXML
    private VBox listRoomVBox;
    @FXML
    private ScrollPane chatListScroll;
    static boolean start = false;
    static Room mainRoom;
    public Client client;
    @FXML
    private Image imageVT;
    @FXML
    private Image imageDT;

    public void setMainRoom(Room mainRoom) {
        HomeController.mainRoom = mainRoom;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client = StartEverything.getSocketController().getClient();
        setup();
        if(mainRoom == null) main_name.setText("room trong");
        else main_name.setText(findRoomName(mainRoom));
        int i = 1;
        for(Room room : StartEverything.getSocketController().getConnectedServer().getRooms()){
            viewListRoom(room);
        }
        for(Client client1 : StartEverything.getSocketController().getConnectedServer().getClients()){
            viewItemUserOnl(client1);
        }
        if(start){
            addMessage();
        }
    }

    public void setup(){
        titlep.setVisible(start);
        footerp.setVisible(start);
        ChatList.setVisible(start);
        Stage stage1 = StartEverything.getSocketController().getStage();
        stage1.setTitle(client.getName());
    }

    public String findRoomName(Room room){
        if (room.getType().equals("private")){
            for(Client client1 : room.getClients()){
                if (!client1.getId().equals(client.getId())){
                    return client1.getName();
                }
            }
        }
        else return room.getName();
        return "loi";
    }
    public void viewItemUserOnl(Client client1){
        if(client1.isStatus()){
            VBox itemUser = new VBox();
            itemUser.setPrefWidth(65);
            itemUser.setPrefHeight(60);
            itemUser.setAlignment(Pos.TOP_CENTER);
            ImageView imageView = new ImageView(imageDT);
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);
            Circle cir = new Circle();
            cir.setRadius(5);
            Pane ava = new Pane();
            ava.setPrefHeight(45);
            ava.setPrefWidth(45);
            ava.getChildren().add(imageView);
            imageView.setLayoutX(12);
            ava.getChildren().add(cir);
            cir.setLayoutX(50);
            cir.setLayoutY(40);
            cir.setFill(Paint.valueOf("#37b916"));
            itemUser.getChildren().add(ava);
            Label name = new Label(getLastName(client1.getName()));
            itemUser.getChildren().add(name);
            itemUser.setId(client1.getId());
            itemUser.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    try {
                        openByUser(client1.getName(), client1.getId());
                        start = true;
                        reload();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(client1.getId());
                }
            });
            listUserOnlHBox.getChildren().add(itemUser);
        }
    }

    public void openByUser(String userName, String idUser) throws IOException {
        start = true;
        boolean check = StartEverything.getSocketController().selectUser(idUser);
        if(check){
            mainRoom = RoomController.findPrivateRoom(StartEverything.getSocketController().getRoomList(), idUser);
        }
        else System.out.println("rong");
    }
    public static String getLastName(String name){
        String Lastname = "";
        for(String e : name.split(" ")) Lastname = e;
        return Lastname;
    }

    public void viewListRoom(Room room){
        Pane itemRoom = new Pane();
        ImageView ava = new ImageView(imageVT);
        ava.setFitHeight(50);
        ava.setFitWidth(50);
        itemRoom.getChildren().add(ava);
        ava.setLayoutX(14);
        ava.setLayoutY(12);
        Label name = new Label(findRoomName(room));
        name.setPrefWidth(160);
        name.setStyle("-fx-font-weight: bold;" +
                "    -fx-font-size: 18px;");
        itemRoom.getChildren().add(name);
        name.setLayoutX(81);
        name.setLayoutY(12);
        MessageData messageData = getLastMessageData(room);
        Label lastMessage = new Label(getLastMessageInfor(messageData));
        lastMessage.setStyle("-fx-font-size: 12px;" +
                "    -fx-text-fill: #6d6d6d;");
        lastMessage.setPrefWidth(127);
        itemRoom.getChildren().add(lastMessage);
        lastMessage.setLayoutX(81);
        lastMessage.setLayoutY(39);
        String time = "";
        if(messageData!=null) time = messageData.getSend_time().getHour() + ":"+messageData.getSend_time().getMinute();
        Label timeLastMessage = new Label(time);
        timeLastMessage.setStyle("-fx-font-size: 12px;" +
                "    -fx-text-fill: #6d6d6d;");
        itemRoom.getChildren().add(timeLastMessage);
        timeLastMessage.setLayoutX(216);
        timeLastMessage.setLayoutY(39);
        listRoomVBox.getChildren().add(itemRoom);
        listRoomVBox.setSpacing(5);
        itemRoom.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    start = true;
                    mainRoom = RoomController.findRoom(StartEverything.getSocketController().getRoomList(), room.getId());
                    System.out.println(room.getId());
                    StartEverything.getSocketController().selectRoom(room.getId());
                    reload();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    public String getLastMessageInfor(MessageData messageData){
        String rs = "";
        if(messageData != null){
            if(messageData.getId_user().equals(client.getId())) rs  = "Bạn: ";
            else rs = getLastName(StartEverything.getSocketController().getNameById(messageData.getId_user())) + ": ";
            return rs + messageData.getContent();
        }
        return "";
    }
    public MessageData getLastMessageData(Room room){
        if(room.getMessageDatas().size() == 0) return null;
        return room.getMessageDatas().get(room.getMessageDatas().size()-1);
    }

    public void reload() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(index.class.getResource("home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1150, 800);
        scene.getStylesheets().add(index.class.getResource("home.css").toExternalForm());
        Stage stage1 = StartEverything.getSocketController().getStage();
        stage1.setScene(scene);
    }
    @FXML
    private Image docx;
    @FXML
    private Image xlsx;
    @FXML
    private Image txt;
    @FXML
    private Image pdf;
    static ArrayList<MessageData> ListMessage;
    public void addMessage() {
        ListMessage = StartEverything.getSocketController().getMessageData(mainRoom.getId());
        for (MessageData message : ListMessage) {
            String time = message.getSend_time().getHour() + ":" + message.getSend_time().getMinute();
            String name = StartEverything.getSocketController().getNameById(message.getId_user());
            HBox other = new HBox();
            VBox vb = new VBox();
            HBox hBox = new HBox();
            vb.setId("vb");
            if(name!=null){
                Label namelb = new Label(name);
                vb.getChildren().add(namelb);
            }
            Label lb = new Label(message.getContent());
            if(message.getMessType().equals("text")) vb.getChildren().add(lb);
            else if(message.getMessType().equals("file")){
                ImageView fileIcon = new ImageView(getIconByTypeFile(message.getContent()));
                fileIcon.setFitHeight(40);
                fileIcon.setFitWidth(35);
                hBox.getChildren().add(fileIcon);
                hBox.getChildren().add(lb);
                vb.getChildren().add(hBox);
                hBox.setMaxWidth(250);
            }
            Label lb1 = new Label(time);
            lb1.setId("time");
            vb.getChildren().add(lb1);
            if (name == null) {
                hBox.setAlignment(Pos.BOTTOM_LEFT);
                vb.setAlignment(Pos.BOTTOM_RIGHT);
                hBox.setId("BoxMessageMe");
                lb.setId("BoxMessageMe");
                ChatList.getChildren().add(vb);
            } else {
                hBox.setAlignment(Pos.BOTTOM_LEFT);
                vb.setAlignment(Pos.BOTTOM_LEFT);
                hBox.setId("BoxMessage");
                lb.setId("BoxMessage");
                ChatList.getChildren().add(other);
                ImageView ava = new ImageView(imageVT);
                ava.setFitWidth(45);
                ava.setFitHeight(45);
                other.setAlignment(Pos.CENTER_LEFT);
                other.setPadding(new Insets(10));
                other.getChildren().add(ava);
                other.getChildren().add(vb);
            }
            onClickFileMessage(message, vb);
        }
    }
    public Image getIconByTypeFile(String fileName){
        String type = "";
        for (String s : fileName.split("\\.")) type = s;
        if(type.equals("docx")) return docx;
        else if (type.equals("xlsx")) return xlsx;
        else if (type.equals("pdf")) return pdf;
        else if (type.equals("txt")) return txt;
        return null;
    }
    public void onClickFileMessage(MessageData message, VBox vb){
        if(message.getMessType().equals("file")){
            vb.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    DirectoryChooser dc = new DirectoryChooser();
                    dc.setTitle("Chọn nơi để lưu tài liệu");
                    File file = dc.showDialog(StartEverything.getSocketController().getStage());
                    String filePath = file.getPath();
                    // System.out.println(filePath);
                    StartEverything.getSocketController().downloadFile(mainRoom.getId(), 1, message.getContent(), filePath);
                }
            });
        }
    }
    public void send_(KeyEvent e) throws IOException {
        if(e.getCode() == KeyCode.ENTER){
            send();
            reload();
        }
    }

    public void send() {
        String contentChat = content.getText();
        System.out.println(contentChat);
        StartEverything.getSocketController().clickEnterChat(mainRoom.getId(), contentChat);
    }

    @FXML
    private VBox listUser;

    private static ArrayList<CheckBox> CBList;
    public void openMenu(){
        if (addUserMenu.getLayoutX() != 850){
            addUserMenu.setLayoutX(850);
        }
        else addUserMenu.setLayoutX(1160);
        addUserMenu.toFront();
        CBList = new ArrayList<CheckBox>();
        for(Client client1 : StartEverything.getSocketController().getConnectedServer().getClients()){
            HBox itemUserHBox = new HBox();
            itemUserHBox.setId("itemUserHBox");
            itemUserHBox.setAlignment(Pos.CENTER_LEFT);
            ImageView ava = new ImageView(imageVT);
            ava.setFitWidth(40);
            ava.setFitHeight(40);
            itemUserHBox.getChildren().add(ava);
            Label nameLable = new Label(client1.getName());
            nameLable.setId("nameLable");
            itemUserHBox.getChildren().add(nameLable);
            CheckBox CB = new CheckBox();
            itemUserHBox.getChildren().add(CB);
            listUser.getChildren().add(itemUserHBox);
            CBList.add(CB);
        }
    }
    public void addUsers(){
        int i = 0;
        ArrayList<Client> clients = StartEverything.getSocketController().getConnectedServer().getClients();
        ArrayList<Client> clientSelected = new ArrayList<Client>();
        for(CheckBox CB:CBList){
            if(CB.isSelected()==true){
                System.out.println(clients.get(i).getId());
                clientSelected.add(clients.get(i));
            }
            i++;
        }
        StartEverything.getSocketController().createGroup(clientSelected);
    }
    public void uploadFile(){
        FileChooser fc = new FileChooser();
        fc.setTitle("Chọn file để tải lên");
        FileChooser.ExtensionFilter fileFilter = new FileChooser.ExtensionFilter("Kiểu tệp", "*.pdf", "*.txt", "*.docx", "*.xlsx");
        fc.getExtensionFilters().add(fileFilter);
        File file = fc.showOpenDialog(StartEverything.getSocketController().getStage());
        if (file != null){
            String fileName = file.getName();
            String filePath = file.getPath();
            StartEverything.getSocketController().sendFileToRoom(mainRoom.getId(), fileName, filePath);
        }
    }
    public void uploadImage(){
        FileChooser fc = new FileChooser();
        fc.setTitle("Chọn ảnh để tải lên");
        FileChooser.ExtensionFilter fileFilter = new FileChooser.ExtensionFilter("Kiểu tệp", "*.png", "*.jpg");
        fc.getExtensionFilters().add(fileFilter);
        File file = fc.showOpenDialog(StartEverything.getSocketController().getStage());
        if (file != null){
            String fileName = file.getName();
            String filePath = file.getPath();
            StartEverything.getSocketController().sendFileToRoom(mainRoom.getId(), fileName, filePath);
        }
    }
}
