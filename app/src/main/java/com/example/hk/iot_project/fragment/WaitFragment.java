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
import android.widget.EditText;

import com.example.hk.iot_project.R;
import com.example.hk.iot_project.activity.MainActivity;

import static android.content.Context.MODE_PRIVATE;

public class WaitFragment extends Fragment {
    public View view;
    private String ipAddress;
    private SharedPreferences prefs;

    // UI 관련
    private EditText et_setip;
    private Button btn_setip;

    public static WaitFragment newInstance() {
        Log.d("onWaitFragment", "iot complete is failed");
        return new WaitFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("onWaitFragment", "iot complete is failed1");
        view = inflater.inflate(R.layout.fragment_wait, container, false);

        // Shares Preference
        prefs = this.getActivity().getSharedPreferences("IoTguanaSettings", MODE_PRIVATE);
        ipAddress = prefs.getString("key_ipAddress","192.168.0.6");

        et_setip = view.findViewById(R.id.et_setip);
        btn_setip = view.findViewById(R.id.btn_setip);

        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       android.text.Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend);
                    if (!resultingTxt.matches ("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (int i=0; i<splits.length; i++) {
                            if (Integer.valueOf(splits[i]) > 255) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }
        };
        et_setip.setFilters(filters);

        btn_setip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIp();
            }
        });

        return view;
    }

    public void setIp(){
        ipAddress = String.valueOf(et_setip.getText());

        // Shares Preference 수정
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString("key_ipAddress", ipAddress);
        ed.apply();

        ((MainActivity)getActivity()).setIpAddress();
        ((MainActivity)getActivity()).transmitPacket();
//        ((MainActivity)getActivity()).receivePacket();

        Log.i("onWaitFragment","getText of ip address : "+ipAddress);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("onWaitFragment", "iot complete is failed2");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
