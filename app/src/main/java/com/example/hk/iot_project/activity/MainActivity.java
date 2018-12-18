package com.example.hk.iot_project.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.hk.iot_project.R;
import com.example.hk.iot_project.fragment.MainFragment;
import com.example.hk.iot_project.fragment.SettingFragment;
import com.example.hk.iot_project.fragment.WaitFragment;

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


public class MainActivity extends AppCompatActivity {
    private long time = 0;
    private int naviStatus = 1;
    private boolean connectStatus = false;
    private SharedPreferences prefs;

    //  UDP연결 관련
    private DatagramPacket packet;
    private DatagramSocket socket;
    private InetAddress serverAddr;
    private int sPORT = 8880;
    private byte[] buf;

    // set socketData
    private String sleeping_min = "";
    private String sleeping_max = "";
    private String brights_state = "";
    private String window = "";
    private String temperature_min = "";
    private String temperature_max = "";
    private String temperature_state = "";
    private String humidity_min = "";
    private String humidity_max = "";
    private String str_graph_temperature;
    private String str_graph_humidity;

    // local 변수
    private String ipAddress;

    // BottomNavigationView
    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Shares Preference
        prefs = getSharedPreferences("IoTguanaSettings", MODE_PRIVATE);
        ipAddress = prefs.getString("key_ipAddress", "192.168.0.6");
        sleeping_min = prefs.getString("key_sleeping_min", "22");
        sleeping_max = prefs.getString("key_sleeping_max", "5");
        brights_state = prefs.getString("key_brights_state", "1");
        window = prefs.getString("key_window", "1");
        temperature_min = prefs.getString("key_temperature_min", "25");
        temperature_max = prefs.getString("key_temperature_max", "30");
        temperature_state = prefs.getString("key_temperature_state", "1");
        humidity_min = prefs.getString("key_humidity_min", "50");
        humidity_max = prefs.getString("key_humidity_max", "70");

        try {
            socket = new DatagramSocket();
            serverAddr = InetAddress.getByName(ipAddress);
            packet = new DatagramPacket(buf, buf.length, serverAddr, sPORT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("onMainActivity","ip address : "+ipAddress);
        receivePacket();
        transmitPacket();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, WaitFragment.newInstance())
                    .commitNow();
        }

        // navigationBar set
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        if (naviStatus != 1 && connectStatus) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, MainFragment.newInstance())
                                    .commitNow();
                            naviStatus = 1;
                        }
                        return true;
                    case R.id.navigation_Setting:
                        if (naviStatus != 2 && connectStatus) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, SettingFragment.newInstance())
                                    .commitNow();
                            naviStatus = 2;
                        }
                        return true;
                    case R.id.navigation_Post:
                        if (connectStatus) {
                            Intent intent = new Intent(MainActivity.this, ChartActivity.class);
                            intent.putExtra("temperatureStr", str_graph_temperature);
                            intent.putExtra("humidityStr", str_graph_humidity);
                            startActivity(intent);
                        }
                        return true;
                }
                return false;
            }
        };
        navigation.getMenu().getItem(2).setCheckable(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void setIpAddress() {
        ipAddress = prefs.getString("key_ipAddress", "192.168.0.6");

        try {
            serverAddr = InetAddress.getByName(ipAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        receivePacket();
        transmitPacket();
    }

    public void transmitPacket() {
        // udp 송신 thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    buf = ("~0~").getBytes();
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
                                    buf = ("0001110001110001110001110001110001110001110001110001110001110001110001110001110001110001110001110001").getBytes();
                                    packet = new DatagramPacket(buf, buf.length, serverAddr, sPORT);
                                    socket.receive(packet);
                                    String msg = new String(packet.getData());
                                    subscriber.onNext(msg);
//                                    sleep(100);
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
                                Log.d("onMainActivity", "onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                // 처리시 예외가 발생하면 자동적으로 onError가 호출됨
                            }

                            @Override
                            public void onNext(String msg) {
                                connectStatus = true;

                                String[] strBase = msg.split("~");
                                sleeping_min = strBase[6];
                                sleeping_max = strBase[7];
                                brights_state = strBase[8];
                                window = strBase[3];
                                temperature_min = strBase[9];
                                temperature_max = strBase[10];
                                temperature_state = strBase[11];
                                humidity_min = strBase[12];
                                humidity_max = strBase[13];
                                str_graph_temperature = strBase[15];
                                str_graph_humidity = strBase[16];

                                // Shares Preference 수정
                                SharedPreferences.Editor ed = prefs.edit();
                                ed.putString("key_sleeping_min", sleeping_min);
                                ed.putString("key_sleeping_max", sleeping_max);
                                ed.putString("key_brights_state", brights_state);
                                ed.putString("key_window", window);
                                ed.putString("key_temperature_min", temperature_min);
                                ed.putString("key_temperature_max", temperature_max);
                                ed.putString("key_temperature_state", temperature_state);
                                ed.putString("key_humidity_min", humidity_min);
                                ed.putString("key_humidity_max", humidity_max);
                                humidity_min = prefs.getString("key_humidity_min", "50");
                                humidity_max = prefs.getString("key_humidity_max", "70");
                                ed.apply();

                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.container, MainFragment.newInstance())
                                        .commitNow();
                            }
                        });
            }
        }).start();
    }

    // 뒤로가기 2번 빠르게 누르면 앱 종료
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - time >= 2000) {
            time = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "뒤로 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() - time < 2000) {
            finishAffinity();
        }
    }
}
