package com.example.hk.iot_project.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hk.iot_project.R;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static android.content.Context.MODE_PRIVATE;

public class SettingFragment extends Fragment {
    private View view;
    private SharedPreferences prefs;

    //  UDP연결 관련
    DatagramPacket packet;
    private DatagramSocket socket;
    private InetAddress serverAddr;
    private int sPORT = 8880;
    private byte[] buf;

    // UI 관련
    private EditText et_sleeping_min;
    private EditText et_sleeping_max;
    private TextView tv_brights_power;
    private SeekBar sbar_brights;
    private Switch sw_brights;
    private TextView tv_wind;
    private Switch sw_wind;
    private EditText et_temperature_min;
    private EditText et_temperature_max;
    private Switch sw_temperature;
    private EditText et_humidity_min;
    private EditText et_humidity_max;
    private Switch sw_humidity;
    private Button btn_setting;

    // switch state
    private boolean sw_state_brights;
    private boolean sw_state_wind;
    private boolean sw_state_temperature;
    private boolean sw_state_humidity;

    // send socket
    private String sleeping_min = "";
    private String sleeping_max = "";
    private String brights_state = "";
    private String window = "";
    private String temperature_min = "";
    private String temperature_max = "";
    private String temperature_state = "";
    private String humidity_min = "";
    private String humidity_max = "";
    private String water = "";

    private boolean chk = false;

    // local 변수
    private String ipAddress;
    private int int_seekbar_progress = 0;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("onSettingFragment", "iot complete");
        view = inflater.inflate(R.layout.fragment_setting, container, false);

        // Shares Preference
        prefs = this.getActivity().getSharedPreferences("IoTguanaSettings", MODE_PRIVATE);
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

        et_sleeping_min = view.findViewById(R.id.et_sleeptime_min);
        et_sleeping_max = view.findViewById(R.id.et_sleeptime_max);
        tv_brights_power = view.findViewById(R.id.tv_brights_power);
        sbar_brights = view.findViewById(R.id.sbar_brights);
        sw_brights = view.findViewById(R.id.sw_brights);
        tv_wind = view.findViewById(R.id.tv_wind);
        sw_wind = view.findViewById(R.id.sw_wind);
        et_temperature_min = view.findViewById(R.id.et_temperature_min);
        et_temperature_max = view.findViewById(R.id.et_temperature_max);
        sw_temperature = view.findViewById(R.id.sw_temperature);
        et_humidity_min = view.findViewById(R.id.et_humidity_min);
        et_humidity_max = view.findViewById(R.id.et_humidity_max);
        sw_humidity = view.findViewById(R.id.sw_humidity);
        btn_setting = view.findViewById(R.id.btn_setting);

        // EditText setting
        et_sleeping_min.setHint(sleeping_min);
        et_sleeping_max.setHint(sleeping_max);
        et_temperature_min.setHint(temperature_min);
        et_temperature_max.setHint(temperature_max);
        et_humidity_min.setHint(humidity_min);
        et_humidity_max.setHint(humidity_max);

        if((window.equals("1"))){
            tv_wind.setText("환기 중 입니다.");
        }else {
            tv_wind.setText("창문이 닫혀있습니다.");
        }

        InputFilter[] filters_24 = new InputFilter[1];
        filters_24[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       android.text.Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend);
                    if (!resultingTxt.matches ("^\\d{1,2}?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (int i=0; i<splits.length; i++) {
                            if (Integer.valueOf(splits[i]) > 24 || Integer.valueOf(splits[i]) < 1) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }
        };
        et_sleeping_min.setFilters(filters_24);
        et_sleeping_max.setFilters(filters_24);

        InputFilter[] filters_100 = new InputFilter[1];
        filters_100[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       android.text.Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend);
                    if (!resultingTxt.matches ("^\\d{1,3}?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (int i=0; i<splits.length; i++) {
                            if (Integer.valueOf(splits[i]) > 100) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }
        };

        et_temperature_min.setFilters(filters_100);
        et_temperature_max.setFilters(filters_100);
        et_humidity_min.setFilters(filters_100);
        et_humidity_max.setFilters(filters_100);

        // Seekbar setting
        int_seekbar_progress = Integer.parseInt(brights_state);
        sbar_brights.setProgress(Integer.parseInt(brights_state));
        tv_brights_power.setText(brights_state+"단계");

        // Switch setting
        if(brights_state.equals("0")) {
            sw_state_brights = false;
            sw_brights.setChecked(false);
        }else{
            sw_state_brights = true;
            sw_brights.setChecked(true);
        }

        if(window.equals("1")) {
            sw_state_wind = true;
            sw_wind.setChecked(true);
        }else{
            sw_state_wind = false;
            sw_wind.setChecked(false);
        }

        if(temperature_state.equals("1")) {
            sw_state_temperature = true;
            sw_temperature.setChecked(true);
        }else{
            sw_state_temperature = false;
            sw_temperature.setChecked(false);
        }

        sw_humidity.setChecked(false);

        sw_brights.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sw_state_brights = true;
                    tv_brights_power.setText(int_seekbar_progress+"단계");
                }else{
                    sw_state_brights = false;
                    tv_brights_power.setText("0단계");
                } } });

        sw_wind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sw_state_wind = true;
                }else{
                    sw_state_wind = false;
                } } });

        sw_temperature.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sw_state_temperature = true;
                }else{
                    sw_state_temperature = false;
                } } });

        sw_humidity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sw_state_humidity = true;
                }else{
                    sw_state_humidity = false;
                } } });

        sbar_brights.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                int_seekbar_progress = progress;
                if(progress==0) {
                    sw_state_brights = false;
                    sw_brights.setChecked(false);
                }else {
                    sw_state_brights = true;
                    sw_brights.setChecked(true);
                }
                tv_brights_power.setText(progress + "단계");
            }
        });

        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValue();
                if(Integer.parseInt(sleeping_min)>Integer.parseInt(sleeping_max) && Integer.parseInt(temperature_min) < Integer.parseInt(temperature_max) && Integer.parseInt(humidity_min) < Integer.parseInt(humidity_max)) {
                    try {
                        socket = new DatagramSocket();
                        serverAddr = InetAddress.getByName(ipAddress);
                        buf = (setSocket()).getBytes();
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

                    Toast.makeText(getActivity(), "적용 되었습니다.", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(), "잘못된 값입니다.", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    public String setSocket(){
        String result = "~1";

        result = result+"~"+sleeping_min;           // 1
        result = result+"~"+sleeping_max;
        result = result+"~"+brights_state;
        result = result+"~"+window;
        result = result+"~"+temperature_min;        // 5
        result = result+"~"+temperature_max;
        result = result+"~"+temperature_state;
        result = result+"~"+humidity_min;
        result = result+"~"+humidity_max;
        result = result+"~"+water+"~";              // 10

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

        return result;
    }

    public boolean setValue(){
        if(et_sleeping_min.getText().toString().equals("")){
        }else{
            sleeping_min = et_sleeping_min.getText().toString();
        }

        if(et_sleeping_max.getText().toString().equals("")){
        }else{
            sleeping_max = et_sleeping_max.getText().toString();
        }

        if(sw_state_brights){
            brights_state = String.valueOf(int_seekbar_progress);
        }else{
            brights_state = "0";
        }

        if(sw_state_wind){
            window = "1";
        }else{
            window = "0";
        }

        if(et_temperature_min.getText().toString().equals("")){
        }else{
            temperature_min = et_temperature_min.getText().toString();
        }

        if(et_temperature_max.getText().toString().equals("")){
        }else{
            temperature_max = et_temperature_max.getText().toString();
        }

        if(sw_state_temperature){
            temperature_state = "1";
        }else{
            temperature_state = "0";
        }

        if(et_humidity_min.getText().toString().equals("")){
        }else{
            humidity_min = et_humidity_min.getText().toString();
        }

        if(et_humidity_max.getText().toString().equals("")){
        }else{
            humidity_max = et_humidity_max.getText().toString();
        }

        if(sw_state_humidity){
            water = "1";
        }else{
            water = "0";
        }
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }
}