package com.example.hk.iot_project.model;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.os.SystemClock.sleep;

public class GetDataModel {
    Observable<String> observerable;

    private String str = "this get data";
    private String data = "this is good data";
    private boolean dataIs = false;

    //  UDP연결 관련
    DatagramPacket packet;
    private DatagramSocket socket;
    private InetAddress serverAddr;
    private int sPORT = 8880;
    private String sIP = "192.168.0.6";
    private int cont = 0;

    private byte[] buf;

    public GetDataModel() {
        Log.d("onGetDataModel", "iot complete");
        Log.d("onGetDataModel", "in GetData!");

        try {
            socket = new DatagramSocket();
            serverAddr = InetAddress.getByName(sIP);
            buf = ("hello").getBytes();
            packet = new DatagramPacket(buf, buf.length, serverAddr, sPORT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Observable
                        .create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(Subscriber<? super String> subscriber) {
                                try {
                                    while (true) {
                                        packet = new DatagramPacket(buf, buf.length, serverAddr, sPORT);

                                        Log.d("onGetDataModel", "start receive");
                                        socket.receive(packet);
                                        String msg = new String(packet.getData());
                                        subscriber.onNext(str);
                                        Log.d("onGetDataModel", "in GetData! pie : " + msg);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onCompleted() {
                                // 처리완료 콜백
                                Log.d("onGetDataModel", "onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                // 처리시 예외가 발생하면 자동적으로 onError가 호출됨
                            }

                            @Override
                            public void onNext(String str) {
                                Log.d("onGetDataModel", "onNext : " + str);
                                dataIs = true;
                            }
                        });
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket.send(packet);
                    while (true) {
                        cont++;
                        buf = ("" + cont).getBytes();
                        packet = new DatagramPacket(buf, buf.length, serverAddr, sPORT);
                        socket.send(packet);
                        Log.d("onGetDataModel", "start trans over");
                        sleep(2000);
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public boolean isData() {
        return dataIs;
    }

    public String getData() {
        Log.d("onGetDataModel", "in goodData : " + data);
        return data;
    }
}