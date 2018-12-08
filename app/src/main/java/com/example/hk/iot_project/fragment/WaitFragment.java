package com.example.hk.iot_project.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hk.iot_project.R;

public class WaitFragment extends Fragment {
    private TextView textView;
    public View view;


    public static WaitFragment newInstance() {
        Log.d("onMainFragment", "iot complete is failed");
        return new WaitFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("onMainFragment", "iot complete is failed1");
        view = inflater.inflate(R.layout.fragment_wait, container, false);

        textView = view.findViewById(R.id.tv_playorsleep);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("onMainFragment", "iot complete is failed2");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
