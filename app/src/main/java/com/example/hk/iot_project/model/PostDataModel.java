package com.example.hk.iot_project.model;

import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;

import java.io.PrintWriter;
import java.net.Socket;

public class PostDataModel {
    private int postExample = 33;

    //  TCP연결 관련
    private Socket PostSocket;
    private PrintWriter socketOut;
    private int port = 7777;
    private final String ip = "223.195.222.138";
    private MyHandler myHandler;
    private PostThread postThread;

    public void GetDataModel() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            PostSocket = new Socket(ip, port);
            socketOut = new PrintWriter(PostSocket.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        myHandler = new MyHandler();
        postThread = new PostThread();
        postThread.start();
    }

    class PostThread extends Thread {
        @Override
        public void run() {
            while (true) {
//                try {
//                    // InputStream의 값을 읽어와서 data에 저장
//                    String data = socketIn.readLine();
//                    // Message 객체를 생성, 핸들러에 정보를 보낼 땐 이 메세지 객체를 이용
//                    Message msg = myHandler.obtainMessage();
//                    msg.obj = data;
//                    myHandler.sendMessage(msg);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        }
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
//            tv.setText(msg.obj.toString());
        }
    }
}
