package com.example.hk.iot_project.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;

import com.example.hk.iot_project.R;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SettingFragment extends Fragment {
    private View view;

    //  UDP연결 관련
    DatagramPacket packet;
    private DatagramSocket socket;
    private InetAddress serverAddr;
    private int sPORT = 8880;
    private String sIP = "192.168.0.6";
    private byte[] buf;

    // UI 관련
    private EditText et_sleeping_min;
    private EditText et_sleeping_max;
    private SeekBar sbar_brights;
    private Switch sw_brights;
    private EditText et_temperature_min;
    private EditText et_temperature_max;
    private Switch sw_temperature;
    private EditText et_humidity_min;
    private EditText et_humidity_max;
    private Switch sw_humidity;
    private Button btn_setting;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("onSettingFragment", "iot complete");
        view = inflater.inflate(R.layout.fragment_setting, container, false);

        btn_setting = view.findViewById(R.id.btn_setting);

        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    socket = new DatagramSocket();
                    serverAddr = InetAddress.getByName(sIP);
                    buf = ("this is Setting data").getBytes();
                    packet = new DatagramPacket(buf, buf.length, serverAddr, sPORT);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
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
        });
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }
}
