package com.example.hk.iot_project.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hk.iot_project.R;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainFragment extends Fragment {
    public View view;

    //  UDP연결 관련
    DatagramPacket packet;
    private DatagramSocket socket;
    private InetAddress serverAddr;
    private int sPORT = 8880;
    private String sIP = "192.168.0.6";
    private byte[] buf;

    // soket Strings
    private String str1 = "";
    private String str2 = "";
    private String str3 = "";
    private String str4 = "";

    // UI 관련
    private TextView tv_playorsleep;
    private TextView tv_brights;
    private TextView tv_status_brights;
    private TextView tv_wind;
    private TextView tv_temperature;
    private TextView tv_status_temperature;
    private TextView tv_status_todo_temperature;
    private TextView tv_humidity;
    private TextView tv_status_humidity;
    private TextView tv_status_todo_humidity;
    private ProgressBar progressBar;

    // set parseInt
    private int int_brights;
    private int int_temperature;
    private int int_humidity;

    // UI text
    private String str_playorsleep;
    private String str_status_brights;
    private String str_wind;
    private String str_status_temperature;
    private String str_status_todo_temperature;
    private String str_status_humidity;
    private String str_status_todo_humidity;

    public static MainFragment newInstance() {
        Log.d("onMainFragment", "iot complete");
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("onMainFragment", "iot complete1");
        view = inflater.inflate(R.layout.fragment_main, container, false);

        tv_playorsleep = view.findViewById(R.id.tv_playorsleep);
        tv_brights = view.findViewById(R.id.tv_brights);
        tv_status_brights = view.findViewById(R.id.tv_status_brights);
        tv_wind = view.findViewById(R.id.tv_wind);
        tv_temperature = view.findViewById(R.id.tv_temperature);
        tv_status_temperature = view.findViewById(R.id.tv_status_temperature);
        tv_status_todo_temperature = view.findViewById(R.id.tv_status_todo_temperature);
        tv_humidity = view.findViewById(R.id.tv_humidity);
        tv_status_humidity = view.findViewById(R.id.tv_status_humidity);
        tv_status_todo_humidity = view.findViewById(R.id.tv_status_todo_humidity);
        progressBar = view.findViewById(R.id.progressBar);

        try {
            socket = new DatagramSocket();
            serverAddr = InetAddress.getByName(sIP);
            buf = ("thisDataSetIsFullSet000111000111000111").getBytes();
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
                                    packet = new DatagramPacket(buf, buf.length, serverAddr, sPORT);
                                    socket.receive(packet);
                                    String msg = new String(packet.getData());
                                    subscriber.onNext(msg);
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
                                Log.d("onMainFragment", "onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                // 처리시 예외가 발생하면 자동적으로 onError가 호출됨
                            }

                            @Override
                            public void onNext(String msg) {
                                String[] strBase = msg.split("@");
                                str1 = strBase[1];
                                str2 = strBase[2];
                                str3 = strBase[3];
                                str4 = strBase[5];

                                setUI();
                                Log.d("onMainFragment", "onNext : 1." + str1 + " 2." + str2 + " 3." + str3 + " 4." + str4);
                            }
                        });
            }
        }).start();

        // 테스트용 udp 송신 thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    buf = ("@this@is@MainFragment@data@27@").getBytes();
                    packet = new DatagramPacket(buf, buf.length, serverAddr, sPORT);
                    socket.send(packet);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return view;
    }

    public void setUI(){
        int_brights = Integer.parseInt(str4);
        int_temperature = Integer.parseInt(str4);
        int_humidity = Integer.parseInt(str4);

        if(str4.equals("27")){
            str_playorsleep = "onPlaying...";
        }else{
            str_playorsleep = "onSleeping";
        }

        if(int_brights >= 70 && int_brights <= 100){
            str_status_brights = "낮의 밝기 입니다.";
        }else{
            str_status_brights = "밤의 밝기 입니다.";
        }

        if(str4.equals("27")){
            str_wind = "환기 중 입니다.";
        }else{
            str_wind = "창문이 닫혀있습니다.";
        }

        if(int_temperature >= 25 && int_temperature <= 30){
            str_status_temperature = "적정 온도 입니다.";
            str_status_todo_temperature = "특별한 조치가 필요하지 않습니다.";
        }else if(int_temperature < 25){
            str_status_temperature = "적정 온도가 아닙니다.";
            str_status_todo_temperature = "스팟을 동작합니다.";
        }else{
            str_status_temperature = "적정 온도가 아닙니다.";
            str_status_todo_temperature = "냉방이 필요합니다.";
        }

        if(int_humidity >= 50 && int_humidity <= 70){
            str_status_humidity = "적정 습도입니다.";
            str_status_todo_humidity = "특별한 조치가 필요하지 않습니다.";
        }else if(int_humidity < 50){
            str_status_humidity = "적정 습도가 아닙니다.";
            str_status_todo_humidity = "분무기를 뿌려 습도를 높입니다.";
        }else{
            str_status_humidity = "적정 습도가 아닙니다.";
            str_status_todo_humidity = "창문을 열어 환기를 시킵니다.";
        }

        progressBar.setVisibility(View.INVISIBLE);

        tv_playorsleep.setText(str_playorsleep);
        tv_brights.setText(int_brights+"%");
        tv_status_brights.setText(str_status_brights);
        tv_wind.setText(str_wind);
        tv_temperature.setText(int_temperature+"℃");
        tv_status_temperature.setText(str_status_temperature);
        tv_status_todo_temperature.setText(str_status_todo_temperature);
        tv_humidity.setText(int_humidity+"%");
        tv_status_humidity.setText(str_status_humidity);
        tv_status_todo_humidity.setText(str_status_todo_humidity);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
