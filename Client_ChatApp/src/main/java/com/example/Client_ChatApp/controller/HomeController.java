package com.example.Client_ChatApp.controller;

import com.example.Client_ChatApp.index;
import com.example.Client_ChatApp.model.Client;
import com.example.Client_ChatApp.model.MessageData;
import com.example.Client_ChatApp.model.Room;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.LongToIntFunction;

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
    @FXML
    private ImageView mainAva;
    @FXML
    private ImageView clientAva;
    @FXML
    private Pane removeButton;
    @FXML
    private Pane renameButton;
    @FXML
    private Label addUserLable;

    public void setMainRoom(Room mainRoom) {
        HomeController.mainRoom = mainRoom;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client = StartEverything.getSocketController().getClient();
        clientAva.setImage(getAvaImage(client.getId()));
        setup();
        if(mainRoom == null) main_name.setText("room trong");
        else{
            main_name.setText(RoomController.findRoomName(mainRoom));
            mainAva.setImage(findAvaRoom(mainRoom));
            if(mainRoom.getType().equals("private")){
                setStatusLb(mainRoom);
                renameButton.setVisible(false);
                removeButton.setVisible(false);
                addUserLable.setText("Tạo nhóm trò chuyện");
            }
            else{
                renameButton.setVisible(true);
                removeButton.setVisible(true);
                addUserLable.setText("Thêm người vào nhóm");
            }
        }
        int i = 1;
        for(Room room : StartEverything.getSocketController().getConnectedServer().getRooms()){
            viewListRoom(room);
        }
        for(Client client1 : StartEverything.getSocketController().getConnectedServer().getClients()){
            viewItemUserOnl(client1);
        }
        if(start){
            if(mainRoom != null) addMessage();
        }
    }

    public void setup(){
        titlep.setVisible(start);
        footerp.setVisible(start);
        ChatList.setVisible(start);
        Stage stage1 = StartEverything.getSocketController().getStage();
        stage1.setTitle(client.getName());
    }
    @FXML
    Image avaImage;
    @FXML
    Image avaGroupImg;
    static String pathForShowImage;
    public String getPathForShowImage(){return pathForShowImage;}
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
        pathForShowImage = path;
        path = path + "/" + "ava" + idUser + ".jpg";
        Image image = new Image(path);
        if(image.isError()){
            return avaImage;
        }
        return image;
    }

    public Image findAvaRoom(Room room){
        if(room.getType().equals("private")){
            for(Client client1 : room.getClients()){
                if(!client1.getId().equals(client.getId())){
                    Image image = new Image(getAvaImage(client1.getId()).getUrl());
                    return image;
                }
            }
        }
        return avaGroupImg;
    }
    @FXML
    private Label statusLb;
    public void setStatusLb(Room room){
        for (Client client1 :room.getClients()){
            if (!client1.getId().equals(client.getId())){
                if (client1.isStatus()) statusLb.setText("Đang hoạt động");
                else statusLb.setText("Không hoạt động");
            }
        }
    }
    public void viewItemUserOnl(Client client1){
        if(client1.isStatus()){
            VBox itemUser = new VBox();
            itemUser.setPrefWidth(65);
            itemUser.setPrefHeight(60);
            itemUser.setAlignment(Pos.TOP_CENTER);
            ImageView imageView = new ImageView(getAvaImage(client1.getId()));
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
            Label name = new Label(ClientController.getLastName(client1.getName()));
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
        StartEverything.getSocketController().selectUser(idUser);
        mainRoom = RoomController.findPrivateRoom(StartEverything.getSocketController().getRoomList(), idUser);
    }
    public void viewListRoom(Room room){
        Pane itemRoom = new Pane();
        ImageView ava = new ImageView(findAvaRoom(room));
        ava.setFitHeight(50);
        ava.setFitWidth(50);
        itemRoom.getChildren().add(ava);
        ava.setLayoutX(14);
        ava.setLayoutY(12);
        Label name = new Label(RoomController.findRoomName(room));
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
            else rs = ClientController.getLastName(StartEverything.getSocketController().getNameById(messageData.getId_user())) + ": ";
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
    @FXML
    private Image mp3;
    @FXML
    private Image jpg;
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
            else if(message.getMessType().equals("file")||message.getMessType().equals("audio")){
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
                ImageView ava = new ImageView(getAvaImage(message.getId_user()));
                ava.setFitWidth(45);
                ava.setFitHeight(45);
                other.setAlignment(Pos.CENTER_LEFT);
                other.setPadding(new Insets(10));
                other.getChildren().add(ava);
                other.getChildren().add(vb);
            }
            onClickFileMessage(message, hBox);
        }
    }
    public Image getIconByTypeFile(String fileName){
        String type = "";
        for (String s : fileName.split("\\.")) type = s;
        if(type.equals("docx")) return docx;
        else if (type.equals("xlsx")) return xlsx;
        else if (type.equals("pdf")) return pdf;
        else if (type.equals("txt")) return txt;
        else if (type.equals("mp3")) return mp3;
        else if (type.equals("jpg")||type.equals("png")) return jpg;
        return null;
    }
    public void onClickFileMessage(MessageData message, HBox hb){
        if(message.getMessType().equals("file")){//||message.getMessType().equals("audio")
            hb.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    DirectoryChooser dc = new DirectoryChooser();
                    dc.setTitle("Chọn nơi để lưu tài liệu");
                    File file = dc.showDialog(StartEverything.getSocketController().getStage());
                    if(file != null){
                        String filePath = file.getPath();
                        // System.out.println(filePath);
                        StartEverything.getSocketController().downloadFile(mainRoom.getId(), 1, message.getContent(), filePath);
                    }
                }
            });
        }
        else if(message.getMessType().equals("audio")){
            hb.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    Room room = RoomController.findRoom(StartEverything.getSocketController().getConnectedServer().getRooms(),mainRoom.getId());

                    StartEverything.getSocketController().playAudio(room.getId(), message.getContent());
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
    @FXML
    private VBox Menu;
    public void openMenu() throws IOException {
        if (Menu.getLayoutX() == 1160){
            Menu.setLayoutX(885);
            addUserMenu.setLayoutX(1160);
            removeUserMenu.setLayoutX(1160);
            renamePane.setLayoutX(1160);
            //Menu.toFront();
            return;
        }
        Menu.setLayoutX(1160);
    }
    public void openAddUser(){
        Menu.setLayoutX(1160);
        if (addUserMenu.getLayoutX() != 850){
            addUserMenu.setLayoutX(850);
        }
        else addUserMenu.setLayoutX(1160);
        addUserMenu.toFront();
        CBList = new ArrayList<CheckBox>();
        ArrayList<Client> listUserToAdd = new ArrayList<Client>();
        listUserToAdd  = RoomController.listUserRemoveListUser(StartEverything.getSocketController().getConnectedServer().getClients(), mainRoom.getClients());
        for(Client client1 : listUserToAdd){
            HBox itemUserHBox = new HBox();
            itemUserHBox.setId("itemUserHBox");
            itemUserHBox.setAlignment(Pos.CENTER_LEFT);
            ImageView ava = new ImageView(getAvaImage(client1.getId()));
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
        ArrayList<Client> listUserToAdd = new ArrayList<Client>();
        listUserToAdd  = RoomController.listUserRemoveListUser(StartEverything.getSocketController().getConnectedServer().getClients(), mainRoom.getClients());
        ArrayList<Client> clientSelected = new ArrayList<Client>();
        for(CheckBox CB : CBList){
            if(CB.isSelected()==true){
                System.out.println(listUserToAdd.get(i).getId());
                clientSelected.add(listUserToAdd.get(i));
            }
            i++;
        }

        if(mainRoom.getType().equals("private")){
            for(Client client1 : mainRoom.getClients()){
                if(!client1.getId().equals(client.getId())) clientSelected.add(client1);
            }
            StartEverything.getSocketController().createGroup(clientSelected);
        }
        else if(mainRoom.getType().equals("group")){
            StartEverything.getSocketController().addToGroup(mainRoom.getId(), clientSelected);
        }
    }
    @FXML
    private Pane removeUserMenu;
    private static ArrayList<CheckBox> CBListRe;
    @FXML
    private VBox listUser2;
    public void openRemoveUser(){
        Menu.setLayoutX(1160);
        if (removeUserMenu.getLayoutX() != 850){
            removeUserMenu.setLayoutX(850);
        }
        else removeUserMenu.setLayoutX(1160);
        removeUserMenu.toFront();
        CBListRe = new ArrayList<CheckBox>();
        for(Client client1 : mainRoom.getClients()){
            if(!client1.getId().equals(client.getId())){
                HBox itemUserHBox = new HBox();
                itemUserHBox.setId("itemUserHBox");
                itemUserHBox.setAlignment(Pos.CENTER_LEFT);
                ImageView ava = new ImageView(getAvaImage(client1.getId()));
                ava.setFitWidth(40);
                ava.setFitHeight(40);
                itemUserHBox.getChildren().add(ava);
                Label nameLable = new Label(client1.getName());
                nameLable.setId("nameLable");
                itemUserHBox.getChildren().add(nameLable);
                CheckBox CB = new CheckBox();
                itemUserHBox.getChildren().add(CB);
                listUser2.getChildren().add(itemUserHBox);
                CBListRe.add(CB);
            }
        }
    }
    public void removeUsers(){
        int i = 0;
        ArrayList<Client> listUserToRemove = new ArrayList<Client>();
        for(Client client1 : mainRoom.getClients()){
            if(!client1.getId().equals(client.getId())) listUserToRemove.add(client1);
        }
        ArrayList<Client> clientSelected = new ArrayList<Client>();
        for(CheckBox CB : CBListRe){
            if(CB.isSelected()==true){
                System.out.println(listUserToRemove.get(i).getId());
                clientSelected.add(listUserToRemove.get(i));
            }
            i++;
        }

        if(mainRoom.getType().equals("private")){
            //Khong lam gi ca!
        }
        else if(mainRoom.getType().equals("group")){
            //StartEverything.getSocketController().addToGroup(mainRoom.getId(), clientSelected);
        }
    }
    @FXML
    private Pane renamePane;
    @FXML
    private TextField nameRoomTxt;
    public void openRenamePane(){
        Menu.setLayoutX(1160);
        if (renamePane.getLayoutX() != 850){
            renamePane.setLayoutX(850);
        }
        else renamePane.setLayoutX(1160);
        renamePane.toFront();
        nameRoomTxt.setText(mainRoom.getName());
    }

    public void renameRoom() throws IOException {
        mainRoom.setName(nameRoomTxt.getText());
        StartEverything.getSocketController().requestEditRoomName(mainRoom);
        reload();
    }

    public void renameRoom_(KeyEvent e) throws IOException {
        if(e.getCode() == KeyCode.ENTER) renameRoom();
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

    public void openEditPage(MouseEvent mouseEvent) throws IOException {
        Stage stage1 = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(index.class.getResource("editProfile.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1150, 800);
        stage1.setScene(scene);
    }
    @FXML
    private Pane recordBar;
    @FXML
    private Label time;
    @FXML
    private HBox timelineBar;
    @FXML
    private Pane pauseBtn;
    boolean isRecord = false;
    AudioController audioController;
//    byte[] AudioByte = new byte[1024];
    public void startRecord(){
        isRecord = true;
        audioController = new AudioController();
        audioController.startRecord();

        footerp.setLayoutY(843);
        recordBar.setLayoutY(743);
        long startTime = System.currentTimeMillis();
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);

        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
            // Cập nhật thời gian hiện tại
            double elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0;
            if(elapsedTime>10)time.setText("00:"+String.format("%.0f", elapsedTime));
            else time.setText("00:0"+String.format("%.0f", elapsedTime));
            Pane pane = new Pane();
            pane.setPrefWidth(666/60);
            pane.setBackground(Background.fill(Paint.valueOf("#f1c40f")));
            timelineBar.getChildren().add(pane);
            if(elapsedTime >= 60) {
                timeline.pause();
                pauseBtn.setVisible(false);
                time.setText("01:00");
            }
        }));
        timeline.play();
        time.setUserData(timeline);
    }
    public void pauseRecord(){
        isRecord = false;
        // byte[] AudioByte = AudioController.stopRecord();
        audioController.stopRecord();
        Timeline timeline = (Timeline) time.getUserData();
        pauseBtn.setVisible(false);
        timeline.pause();
    }

    public void stopRecord() throws IOException {
        Timeline timeline = (Timeline) time.getUserData();
        timeline.stop();
        reload();
    }
    public void sendRecordToRoom(){
        isRecord = false;
        // byte[] AudioByte = AudioController.stopRecord();
        audioController.stopRecord();
        Timeline timeline = (Timeline) time.getUserData();
        timeline.stop();
        try {
            reload();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        StartEverything.getSocketController().sendAudioToRoom(mainRoom.getId());
    }
}
