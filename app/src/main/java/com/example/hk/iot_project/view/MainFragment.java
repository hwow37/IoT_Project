package com.example.hk.iot_project.view;

import android.arch.lifecycle.ViewModelProviders;
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
import com.example.hk.iot_project.viewmodel.MainViewModel;

import rx.Observable;


public class MainFragment extends Fragment {
    private TextView textView;
    public View view;

    private MainViewModel mViewModel;
    private Observable observable;

    private String str = "this is no data";

    public static MainFragment newInstance() {
        Log.d("onMainFragment", "iot complete");
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("onMainFragment", "iot complete1");
        view = inflater.inflate(R.layout.main_fragment, container, false);

        textView = view.findViewById(R.id.tv_playorsleep);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("onMainFragment", "iot complete2");

        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
//        sleep(500);
        mViewModel.setGetDataModel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
