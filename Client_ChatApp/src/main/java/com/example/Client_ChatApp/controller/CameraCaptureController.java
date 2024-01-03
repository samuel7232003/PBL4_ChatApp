package com.example.Client_ChatApp.controller;

import javafx.application.Platform;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraCaptureController extends JFrame{
    //camera screen
    private static int idCapture = 0;
    private JLabel cameraScreen;
    private JButton btnCapture;
    private VideoCapture capture;
    private Mat image;
    private static boolean sendOK = false;
    private boolean clicked = false;
    public CameraCaptureController(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        setLayout(null);
        cameraScreen = new JLabel();
        cameraScreen.setBounds(0,0,640,480);
        add(cameraScreen);

        btnCapture = new JButton("Chụp");
        btnCapture.setBounds(300, 480, 80, 40);
        add(btnCapture);

        capture = new VideoCapture(0);

        btnCapture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clicked = true;
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stopCapture();
            }
        });
        setSize(new Dimension(640, 560));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    // create camera
    public void startCamera(){
        capture = new VideoCapture(0);
        image = new Mat();
        byte[] imageData;

        ImageIcon icon;
        while (true){

            capture.read(image);
            final MatOfByte buf = new MatOfByte();
            Imgcodecs.imencode(".jpg", image, buf);

            imageData = buf.toArray();

            //add to JLabel
            icon = new ImageIcon(imageData);
            cameraScreen.setIcon(icon);
            //capture and save to file
            if(clicked){
                sendOK = true;
                String name = "ImageCapture" + ++idCapture;
                //write to file
                Imgcodecs.imwrite("images/" + name + ".jpg", image);
                clicked = false;
                stopCapture();
                break;
            }
        }
    }

    public void stopCapture(){
        capture.release();
        image.release();
        setVisible(false);
    }

    public static boolean isSendOK() {
        return sendOK;
    }

    public static void setSendOK(boolean sendOK) {
        CameraCaptureController.sendOK = sendOK;
    }

    public static int getIdCapture() {
        return idCapture;
    }

    public static void setIdCapture(int idCapture) {
        CameraCaptureController.idCapture = idCapture;
    }
}
