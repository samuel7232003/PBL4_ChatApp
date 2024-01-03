package com.example.Client_ChatApp.controller;

import javax.sound.sampled.*;
import java.io.*;

public class AudioController {
    private AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
    private DataLine.Info dataInfo;
    private boolean isRecording = false;
    private TargetDataLine targetLine;
    private Thread recordThread;
    public void startRecord(){
        try {
            dataInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            if(!AudioSystem.isLineSupported(dataInfo)) System.out.println("Not Supported");
            targetLine = (TargetDataLine) AudioSystem.getLine(dataInfo);
            targetLine.open();
            targetLine.start();
            recordThread = new Thread(){
                @Override
                public void run() {
                    AudioInputStream recordingStream = new AudioInputStream(targetLine);
                    File outputFile = new File("Audio/record.wav");
                    try{
                        AudioSystem.write(recordingStream, AudioFileFormat.Type.WAVE, outputFile);
                    }catch (IOException ex){
                        System.out.println(ex);
                    }
                    System.out.println("Stop recording");
                }
            };
            recordThread.start();
            isRecording = true;
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
    public void stopRecord(){
        isRecording = false;
        targetLine.stop();
        targetLine.close();
    }
    public static Clip playMusic(String filepath){
        try {
            File musicPath = new File(filepath);
            if(musicPath.exists()){
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                return clip;
            }

        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
}
