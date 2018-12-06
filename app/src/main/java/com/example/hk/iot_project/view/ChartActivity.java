package com.example.hk.iot_project.view;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.hk.iot_project.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {

    // 그래프 작성
    // 온도
    private LineChart temLineChart;
    private ArrayList<Entry> temperatures;
    private LineDataSet temDataset;
    private XAxis temxAxis;
    private YAxis temyLAxis;
    private YAxis temyRAxis;
    private LineData temLineData;
    private Description temDescription;

    // 습도
    private LineChart humLineChart;
    private ArrayList<Entry> humiditys;
    private LineDataSet humDataset;
    private XAxis humxAxis;
    private YAxis humyLAxis;
    private YAxis humyRAxis;
    private LineData humLineData;
    private Description humDescription;

    private String temperatureStr;
    private String humidityStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_activity);

        Intent intent = getIntent();

        temperatureStr = intent.getStringExtra("temperatureStr");
        humidityStr = intent.getStringExtra("humidityStr");

        // 온도 그래프
        temLineChart = findViewById(R.id.ct_temperature);
        humLineChart = findViewById(R.id.ct_humidity);

        temperatures = new ArrayList<>();
        temperatures.add(new Entry(1, 25));
        temperatures.add(new Entry(2, 26));
        temperatures.add(new Entry(3, 28));
        temperatures.add(new Entry(4, 30));
        temperatures.add(new Entry(5, 29));
        temperatures.add(new Entry(6, 28));

        temDataset = new LineDataSet(temperatures, "최근 1시간동안의 10분간격 온도 변화");
        temDataset.setLineWidth(2);
        temDataset.setCircleRadius(6);
        temDataset.setCircleColor(Color.parseColor("#FFF46B6B"));
        temDataset.setCircleColorHole(Color.YELLOW);
        temDataset.setColor(Color.parseColor("#FFF46B6B"));
        temDataset.setDrawCircleHole(true);
        temDataset.setDrawCircles(true);
        temDataset.setDrawHorizontalHighlightIndicator(false);
        temDataset.setDrawHighlightIndicators(false);
        temDataset.setDrawValues(false);

        temDataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        temLineData = new LineData(temDataset);
        temLineChart.setData(temLineData);

        temxAxis = temLineChart.getXAxis();
        temxAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        temxAxis.setTextColor(Color.BLACK);
        temxAxis.setXOffset(5);
        temxAxis.enableGridDashedLine(8, 24, 0);

        temyLAxis = temLineChart.getAxisLeft();
        temyLAxis.setTextColor(Color.BLACK);

        temyRAxis = temLineChart.getAxisRight();
        temyRAxis.setDrawLabels(false);
        temyRAxis.setDrawAxisLine(false);
        temyRAxis.setDrawGridLines(false);

        temDescription = new Description();
        temDescription.setText("");

        temLineChart.getXAxis().setDrawLabels(false);
        temLineChart.setDoubleTapToZoomEnabled(false);
        temLineChart.setDrawGridBackground(false);
        temLineChart.setDescription(temDescription);
//        lineChart.animateX(3000, Easing.EasingOption.EaseInCubic);
        temLineChart.invalidate();


        // 습도 그래프
        humiditys = new ArrayList<>();
        humiditys.add(new Entry(1, 54));
        humiditys.add(new Entry(2, 60));
        humiditys.add(new Entry(3, 65));
        humiditys.add(new Entry(4, 59));
        humiditys.add(new Entry(5, 52));
        humiditys.add(new Entry(6, 50));

        humDataset = new LineDataSet(humiditys, "최근 1시간동안의 10분간격 습도 변화");
        humDataset.setLineWidth(2);
        humDataset.setCircleRadius(6);
        humDataset.setCircleColor(Color.parseColor("#FFA1B4DC"));
        humDataset.setCircleColorHole(Color.BLUE);
        humDataset.setColor(Color.parseColor("#FFA1B4DC"));
        humDataset.setDrawCircleHole(true);
        humDataset.setDrawCircles(true);
        humDataset.setDrawHorizontalHighlightIndicator(false);
        humDataset.setDrawHighlightIndicators(false);
        humDataset.setDrawValues(false);

        humDataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        humLineData = new LineData(humDataset);
        humLineChart.setData(humLineData);

        humxAxis = humLineChart.getXAxis();
        humxAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        humxAxis.setTextColor(Color.BLACK);
        humxAxis.setXOffset(5);
        humxAxis.enableGridDashedLine(8, 24, 0);

        humyLAxis = humLineChart.getAxisLeft();
        humyLAxis.setTextColor(Color.BLACK);

        humyRAxis = humLineChart.getAxisRight();
        humyRAxis.setDrawLabels(false);
        humyRAxis.setDrawAxisLine(false);
        humyRAxis.setDrawGridLines(false);

        humDescription = new Description();
        humDescription.setText("");

        humLineChart.getXAxis().setDrawLabels(false);
        humLineChart.setDoubleTapToZoomEnabled(false);
        humLineChart.setDrawGridBackground(false);
        humLineChart.setDescription(humDescription);
//        lineChart.animateX(3000, Easing.EasingOption.EaseInCubic);
        humLineChart.invalidate();
    }
}
