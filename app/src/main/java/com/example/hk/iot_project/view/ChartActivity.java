package com.example.hk.iot_project.view;

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
    private LineChart lineChart;
    private ArrayList<Entry> entries;
    private LineDataSet lineDataSet;
    private XAxis xAxis;
    private YAxis yLAxis;
    private YAxis yRAxis;
    private LineData lineData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_activity);

        lineChart = findViewById(R.id.chart);

        entries = new ArrayList<>();
        entries.add(new Entry(1, 25));
        entries.add(new Entry(2, 26));
        entries.add(new Entry(3, 28));
        entries.add(new Entry(4, 30));
        entries.add(new Entry(5, 29));
        entries.add(new Entry(6, 28));

        lineDataSet = new LineDataSet(entries, "최근 1시간동안의 10분간격 온도 변화");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setCircleColorHole(Color.BLUE);
        lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setXOffset(5);
        xAxis.enableGridDashedLine(8, 24, 0);

        yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

        yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");

        lineChart.getXAxis().setDrawLabels(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
//        lineChart.animateX(3000, Easing.EasingOption.EaseInCubic);
        lineChart.invalidate();
    }
}
