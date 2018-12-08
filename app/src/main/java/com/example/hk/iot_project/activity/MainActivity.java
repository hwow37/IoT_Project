package com.example.hk.iot_project.activity;

import android.content.Intent;
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

import static android.os.SystemClock.sleep;


public class MainActivity extends AppCompatActivity {
    private long time = 0;
    private int naviStatus = 0;
    private boolean connectStatus = false;

    //  UDP연결 관련
    private DatagramPacket packet;
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

    private String temperatureStr = "05354231328465421351";
    private String humidityStr = "22256648328465421351";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
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
                        intent.putExtra("temperatureStr", temperatureStr);
                        intent.putExtra("humidityStr", humidityStr);
                        startActivity(intent);
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        try {
            socket = new DatagramSocket();
            serverAddr = InetAddress.getByName(sIP);
            buf = ("thisDataSetIsFullSet000111000111000111").getBytes();
            packet = new DatagramPacket(buf, buf.length, serverAddr, sPORT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // udp 수신 thread
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
                                    sleep(100);
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
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.container, MainFragment.newInstance())
                                        .commitNow();

                                String[] strBase = msg.split("@");
                                str1 = strBase[1];
                                str2 = strBase[2];
                                str3 = strBase[3];
                                str4 = strBase[5];

                                Log.d("onMainActivity", "onNext : 1." + str1 + " 2." + str2 + " 3." + str3 + " 4." + str4);
//                                humidityStr = "humidityData";
//                                temperatureStr = "temperatureData";
                            }
                        });
            }
        }).start();


        // 테스트용 udp 송신 thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    buf = ("@this@is@MainActivity@data@25@").getBytes();
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

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, WaitFragment.newInstance())
                    .commitNow();
        }

        // navigationBar set
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
