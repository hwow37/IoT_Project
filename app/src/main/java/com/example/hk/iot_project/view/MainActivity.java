package com.example.hk.iot_project.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hk.iot_project.R;


public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private long time = 0;
    private int naviStatus = 0;

    private String temperatureStr = "05354231328465421351";
    private String humidityStr = "22256648328465421351";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if(naviStatus != 1) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, MainFragment.newInstance())
                                .commitNow();
                        naviStatus = 1;
                    }
                    return true;
                case R.id.navigation_Setting:
                    if(naviStatus != 2) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, SettingFragment.newInstance())
                                .commitNow();
                        naviStatus = 2;
                    }
                    return true;
                case R.id.navigation_Post:
                    Intent intent = new Intent(MainActivity.this,ChartActivity.class);
                    intent.putExtra("temperatureStr",temperatureStr);
                    intent.putExtra("humidityStr",humidityStr);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);


        Log.d("onMainActivity", "iot complete");
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
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
