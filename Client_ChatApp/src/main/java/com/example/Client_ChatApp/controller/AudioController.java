package com.example.Client_ChatApp.controller;

import javax.sound.sampled.*;
import java.io.*;

public class AudioController {
    static Thread recordThread;
    static ByteArrayOutputStream out;
    static boolean isRecording = false;
    public static AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);

    public static void startRecord() {
        out = new ByteArrayOutputStream();
        isRecording = true;

        recordThread = new Thread(() -> {

            TargetDataLine microphone;
            try {
                microphone = AudioSystem.getTargetDataLine(format);

                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                microphone = (TargetDataLine) AudioSystem.getLine(info);
                microphone.open(format);

                int numBytesRead;
                int CHUNK_SIZE = 1024;
                byte[] data = new byte[microphone.getBufferSize() / 5];
                microphone.start();

                int bytesRead = 0;

                try {
                    while (isRecording) {
                        numBytesRead = microphone.read(data, 0, CHUNK_SIZE);
                        bytesRead = bytesRead + numBytesRead;
                        out.write(data, 0, numBytesRead);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                microphone.close();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        });
        recordThread.start();
    }
//public static void startRecord() {
//    isRecording = true;
//    AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
//    DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
//    if (!AudioSystem.isLineSupported(info)) {
//        System.out.println("Line not support");
//    }
//    final TargetDataLine targetDataLine;
//    try {
//        targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
//        targetDataLine.open();
//    } catch (LineUnavailableException e) {
//        throw new RuntimeException(e);
//    }
//    System.out.println("Starting recording");
//    targetDataLine.start();
//    recordThread = new Thread(() -> {
//        AudioInputStream audioInputStream = new AudioInputStream(targetDataLine);
//        File audioFile = new File("record.mav");
//        try {
//            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, audioFile);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    });
//    recordThread.start();
//    targetDataLine.stop();
//    targetDataLine.close();
//    System.out.println("end sound test");
//}

    public static byte[] stopRecord() {
        isRecording = false;
        while (recordThread.isAlive()) {
        }
        return out.toByteArray();
    }
//    public static void stopRecord(){
//        isRecording = false;
//        if (recordThread.isAlive()) {
//            recordThread.stop();
//        }
//    }
    public static void play(byte[] audioData) {
        // Get an input stream on the byte array
        // containing the data
        try {
            AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
            InputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
            AudioInputStream audioInputStream = new AudioInputStream(byteArrayInputStream, format,
                    audioData.length / format.getFrameSize());
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine speaker = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            speaker.open(format);
            speaker.start();
            int cnt = 0;
            byte tempBuffer[] = new byte[10000];
            try {
                while ((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
                    if (cnt > 0) {
                        // Write data to the internal buffer of
                        // the data line where it will be
                        // delivered to the speaker.
                        speaker.write(tempBuffer, 0, cnt);
                    } // end if
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Block and wait for internal buffer of the
            // data line to empty.
            speaker.drain();
            speaker.close();
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        }

    }

    public static int getAudioDuration(byte[] audioBytes) {
        AudioInputStream audioInputStream = new AudioInputStream(new ByteArrayInputStream(audioBytes),
                AudioController.format, audioBytes.length);
        return Math.round(audioInputStream.getFrameLength() / audioInputStream.getFormat().getFrameRate() / 2);
    }
    public static void play(ByteArrayOutputStream receivedBytes) {
        // Get an input stream on the byte array
        // containing the data
        byte[] audioData = receivedBytes.toByteArray();
        try {
            AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
            InputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
            AudioInputStream audioInputStream = new AudioInputStream(byteArrayInputStream, format,
                    audioData.length / format.getFrameSize());
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine speaker = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            speaker.open(format);
            speaker.start();
            int cnt = 0;
            byte tempBuffer[] = new byte[10000];
            try {
                while ((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
                    if (cnt > 0) {
                        // Write data to the internal buffer of
                        // the data line where it will be
                        // delivered to the speaker.
                        speaker.write(tempBuffer, 0, cnt);
                    } // end if
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Block and wait for internal buffer of the
            // data line to empty.
            speaker.drain();
            speaker.close();
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }
}
