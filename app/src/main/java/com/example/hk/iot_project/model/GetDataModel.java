package com.example.hk.iot_project.model;

import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class GetDataModel {

    //  TCP연결 관련
    private Socket GetSocket;
    private BufferedReader socketIn;
    private int port = 7777;
    private final String ip = "223.195.222.138";
    private MyHandler myHandler;
    private GetThread getThread;

    public void GetDataModel() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            GetSocket = new Socket(ip, port);
            socketIn = new BufferedReader(new InputStreamReader(GetSocket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        myHandler = new MyHandler();
        getThread = new GetThread();
        getThread.start();
    }

    class GetThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    // InputStream의 값을 읽어와서 data에 저장
                    String data = socketIn.readLine();
                    // Message 객체를 생성, 핸들러에 정보를 보낼 땐 이 메세지 객체를 이용
                    Message msg = myHandler.obtainMessage();
                    msg.obj = data;
                    myHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
