package com.example.hk.iot_project.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import static android.content.Context.MODE_PRIVATE;

public class MainFragment extends Fragment {
    public View view;
    private SharedPreferences prefs;

    //  UDP연결 관련
    private DatagramPacket packet;
    private DatagramSocket socket;
    private InetAddress serverAddr;
    private int sPORT = 8880;
    private byte[] buf;

    // UI 관련
    private TextView tv_playorsleep;
    private ImageView img_playingIs;
    private TextView tv_brights;
    private TextView tv_state_brights;
    private TextView tv_wind;
    private TextView tv_temperature;
    private TextView tv_state_temperature;
    private TextView tv_state_todo_temperature;
    private TextView tv_humidity;
    private TextView tv_state_humidity;
    private TextView tv_state_todo_humidity;
    private ProgressBar progressBar_MainFrag;

    // set socketData
    private int int_playorsleep;
    private int int_brights;
    private int int_wind;
    private int int_temperature;
    private int int_humidity;

    // UI text
    private String str_playorsleep;
    private String str_state_brights;
    private String str_wind;
    private String str_state_temperature;
    private String str_state_todo_temperature;
    private String str_state_humidity;
    private String str_state_todo_humidity;

    // local 변수
    private String ipAddress;
    private String temperature_min;
    private String temperature_max;
    private String humidity_min;
    private String humidity_max;

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


        // Shares Preference
        prefs = this.getActivity().getSharedPreferences("IoTguanaSettings", MODE_PRIVATE);
        ipAddress = prefs.getString("key_ipAddress","192.168.0.6");
        temperature_min = prefs.getString("key_temperature_min", "25");
        temperature_max = prefs.getString("key_temperature_max", "30");
        humidity_min = prefs.getString("key_humidity_min", "50");
        humidity_max = prefs.getString("key_humidity_max", "70");

        tv_playorsleep = view.findViewById(R.id.tv_playorsleep);
        img_playingIs = view.findViewById(R.id.img_playingIs);
        tv_brights = view.findViewById(R.id.tv_brights);
        tv_state_brights = view.findViewById(R.id.tv_state_brights);
        tv_wind = view.findViewById(R.id.tv_wind);
        tv_temperature = view.findViewById(R.id.tv_temperature);
        tv_state_temperature = view.findViewById(R.id.tv_state_temperature);
        tv_state_todo_temperature = view.findViewById(R.id.tv_state_todo_temperature);
        tv_humidity = view.findViewById(R.id.tv_humidity);
        tv_state_humidity = view.findViewById(R.id.tv_state_humidity);
        tv_state_todo_humidity = view.findViewById(R.id.tv_state_todo_humidity);
        progressBar_MainFrag = view.findViewById(R.id.progressBar_MainFrag);

        try {
            socket = new DatagramSocket();
            serverAddr = InetAddress.getByName(ipAddress);
            packet = new DatagramPacket(buf, buf.length, serverAddr, sPORT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        receivePacket();
        transmitPacket();

        return view;
    }

    public void transmitPacket() {
        // 테스트용 udp 송신 thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] buf = ("~0~").getBytes();
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
    }

    public void receivePacket() {
        // udp 수신 thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                Observable
                        .create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(Subscriber<? super String> subscriber) {
                                try {
                                    buf = ("000111000111000111000111000111000111000111000111000111000111000111000111000111").getBytes();
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
                                String[] strBase = msg.split("~");
                                int_playorsleep = Integer.parseInt(strBase[1]);
                                int_brights = Integer.parseInt(strBase[2]);
                                int_wind = Integer.parseInt(strBase[3]);
                                int_temperature = Integer.parseInt(strBase[4]);
                                int_humidity = Integer.parseInt(strBase[5]);

                                setUI();
                            }
                        });
            }
        }).start();
    }


    public void setUI() {
        if (int_playorsleep == 1) {
            str_playorsleep = "onPlaying...";
            img_playingIs.setImageResource(R.drawable.onplaying);
        } else {
            str_playorsleep = "onSleeping...";
            img_playingIs.setImageResource(R.drawable.onsleeping);
        }

        if (int_brights >= 19 && int_brights <= 100) {
            str_state_brights = "낮의 밝기 입니다.";
        } else {
            str_state_brights = "밤의 밝기 입니다.";
        }

        if (int_wind == 1) {
            str_wind = "환기 중 입니다.";
        } else {
            str_wind = "창문이 닫혀있습니다.";
        }

        if (int_temperature >= Integer.parseInt(temperature_min) && int_temperature <= Integer.parseInt(temperature_max)) {
            str_state_temperature = "적정 온도 입니다.";
            str_state_todo_temperature = "특별한 조치가 필요하지 않습니다.";
        } else if (int_temperature < Integer.parseInt(temperature_min)) {
            str_state_temperature = "적정 온도가 아닙니다.";
            str_state_todo_temperature = "스팟을 동작하여 온도를 높입니다.";
        } else {
            str_state_temperature = "적정 온도가 아닙니다.";
            str_state_todo_temperature = "창문을 열어 온도를 낮춥니다.";
        }

        if (int_humidity >= Integer.parseInt(humidity_min) && int_humidity <= Integer.parseInt(humidity_max)) {
            str_state_humidity = "적정 습도입니다.";
            str_state_todo_humidity = "특별한 조치가 필요하지 않습니다.";
        } else if (int_humidity < Integer.parseInt(humidity_min)) {
            str_state_humidity = "적정 습도가 아닙니다.";
            str_state_todo_humidity = "분무기를 뿌려 습도를 높입니다.";
        } else {
            str_state_humidity = "적정 습도가 아닙니다.";
            str_state_todo_humidity = "창문을 열어 환기를 시킵니다.";
        }

        progressBar_MainFrag.setVisibility(View.INVISIBLE);

        tv_playorsleep.setText(str_playorsleep);
        tv_brights.setText(int_brights + "%");
        tv_state_brights.setText(str_state_brights);
        tv_wind.setText(str_wind);
        tv_temperature.setText(int_temperature + "℃");
        tv_state_temperature.setText(str_state_temperature);
        tv_state_todo_temperature.setText(str_state_todo_temperature);
        tv_humidity.setText(int_humidity + "%");
        tv_state_humidity.setText(str_state_humidity);
        tv_state_todo_humidity.setText(str_state_todo_humidity);
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
