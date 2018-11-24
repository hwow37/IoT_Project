package com.example.hk.iot_project.model;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.os.SystemClock.sleep;

public class GetDataModel {
    Observable<String> observerable;

    private String str = "this is no data";
    private String data = "this is good data";
    private boolean dataIs = false;

    //  TCP연결 관련
    private Socket GetSocket;
    private BufferedReader socketIn;
    private BufferedWriter socketOut;
    private int port = 7777;
    private final String ip = "223.195.222.138";

    public GetDataModel() {
        Log.d("onGetDataModel", "iot complete");
        Log.d("onGetDataModel", "in GetData!");

        try {
            GetSocket = new Socket(ip, port);
            socketIn = new BufferedReader(new InputStreamReader(GetSocket.getInputStream()));
            socketOut = new BufferedWriter(new OutputStreamWriter(GetSocket.getOutputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        // 처음 데이터를 받기 위해 요청
//                        PrintWriter out = new PrintWriter(socketOut, true);
//                        String callData = "0001";
//                        Log.d("onGetDataModel", "callData : "+callData);
//                        out.println(callData);

                        getData();
                        data = "this is very good data";
                        subscriber.onNext(data);
//                        Log.d("onGetDataModel", "in goodData : " + data);
                        while (true) {
                            try {
                                sleep(5000);
                                // InputStream의 값을 읽어와서 data에 저장
                                String data = socketIn.readLine();
                                Log.d("onGetDataModel", "in GetData! : " + data);
                                subscriber.onNext(str);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
//                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        // 처리완료 콜백
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 처리시 예외가 발생하면 자동적으로 onError가 호출됨
                    }

                    @Override
                    public void onNext(String str) {
                        Log.d("onGetDataModel", "onNext : "+str);
                        dataIs = true;
                    }
                });
    }

    public boolean isData() {
        return dataIs;
    }

    public String getData(){
        Log.d("onGetDataModel", "in goodData : " + data);
        return data;
    }
}
