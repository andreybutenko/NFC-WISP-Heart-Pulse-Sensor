package com.example.nfc_wisp_android_app.nfc_wisp_android_app;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoMeasurementFragment extends Fragment {

    private static final String DISPLAY_MESSAGE = "DISPLAY_MESSAGE";
    private static final float AXIS_LABEL_FONT_SIZE = 12f;


    private LineChart infraredLineChart;
    private LineChart redLineChart;
    private static final int WINDOW_SIZE = 200;
    private static final float Y_INITIAL = 1000000f;
    private static final String IR_CHART_TAG = "Infrared Light Data";
    private static final String RD_CHART_TAG = "Red Light Data";
    private static final String IR_DATA_TAG = "IR";
    private static final String RD_DATA_TAG = "RD";


    public DoMeasurementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_do_measurement, container, false);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        String display_message = args.getString(DISPLAY_MESSAGE);
        TextView textView = (TextView) getActivity().findViewById(R.id.myStatusTextView);
        textView.setText(display_message);

        infraredLineChart = (LineChart) getActivity().findViewById(R.id.lineChart_infrared);
        redLineChart = (LineChart) getActivity().findViewById(R.id.lineChart_red);
        // Line chart general configuration
        chartConfig(infraredLineChart, IR_CHART_TAG);
        chartConfig(redLineChart, RD_CHART_TAG);

        // Actual Data Flow
        LineDataSet ir = setInitialData(IR_DATA_TAG, Color.BLUE);
        LineDataSet rd = setInitialData(RD_DATA_TAG, Color.RED);

        infraredLineChart.setData(new LineData(ir));
        redLineChart.setData(new LineData(rd));

        infraredLineChart.invalidate();
        redLineChart.invalidate();
    }


    private LineDataSet setStaticData(int dir, String tag) {
        float fakeTime = 0f;
        Resources res = getResources();
        InputStream input = res.openRawResource(dir);

        Scanner scan = new Scanner(input);
        List<Entry> dataList = new ArrayList<Entry>();

        while(scan.hasNext()) {
            float data = Float.parseFloat(scan.next());
            dataList.add(new Entry(fakeTime, data));
            fakeTime++;
        }
        LineDataSet dataSet = new LineDataSet(dataList, tag);
        dataSetConfig(dataSet, Color.CYAN);
        return dataSet;
    }

    public void onDataSetChange(Entry ir_entry, Entry rd_entry) {
        YAxis ir_y = infraredLineChart.getAxisLeft();
        YAxis rd_y = redLineChart.getAxisLeft();
        ir_y.resetAxisMaximum();
        ir_y.resetAxisMinimum();
        rd_y.resetAxisMaximum();
        rd_y.resetAxisMinimum();
        infraredLineChart.invalidate();
        redLineChart.invalidate();

        ILineDataSet IR = infraredLineChart.getLineData().getDataSetByLabel(IR_DATA_TAG, false);
        ILineDataSet RD = redLineChart.getLineData().getDataSetByLabel(RD_DATA_TAG, false);

        IR.removeFirst();
        RD.removeFirst();


        IR.addEntry(ir_entry);
        RD.addEntry(rd_entry);


        infraredLineChart.notifyDataSetChanged();
        infraredLineChart.fitScreen();
        infraredLineChart.invalidate();
        infraredLineChart.invalidate();


        redLineChart.notifyDataSetChanged();
        redLineChart.fitScreen();
        redLineChart.invalidate();
        redLineChart.invalidate();
    }


    private LineDataSet setInitialData(String name, int color) {
        float fakeTime = 0f;
        int count = 0;
        List<Entry> dataList = new ArrayList<Entry>();

        while(count < WINDOW_SIZE) {
            float data = Y_INITIAL;
            dataList.add(new Entry(fakeTime, data));
            fakeTime++;
            count++;
        }
        LineDataSet dataSet = new LineDataSet(dataList, name);
        dataSetConfig(dataSet, color);
        return dataSet;
    }

    private void dataSetConfig(LineDataSet dataSet, int color) {
        dataSet.setAxisDependency(AxisDependency.LEFT);
        dataSet.setCircleRadius(1f);
        dataSet.setDrawValues(false);
        dataSet.setCircleColor(color);
        dataSet.setColor(color);
    }

    private void chartConfig(LineChart chart, String desc) {
        // General Setting
        Description description = new Description();
        description.setText(desc);
        description.setTextColor(Color.BLACK);
        description.setTextSize(10f);

        chart.setDescription(description);
        chart.setNoDataText(desc);
        chart.setAutoScaleMinMaxEnabled(true);
        chart.setDrawBorders(true);

        chart.setTouchEnabled(false); // disable touch interaction

        // Animation & Viewport
        //chart.animateX(3000);
        chart.setVisibleXRangeMinimum(WINDOW_SIZE);

        chart.getAxisRight().setEnabled(false); // disable the right axis
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setEnabled(true);
        yAxis.setTextColor(Color.BLACK);
        yAxis.setTextSize(AXIS_LABEL_FONT_SIZE);
        yAxis.setLabelCount(7, true);
        yAxis.setDrawGridLines(false); // disable background grid line


        XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(AXIS_LABEL_FONT_SIZE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false); // disable background grid line
        chart.invalidate();
    }

    public void OnTagDetected(String displayMessage, String tagMessage) {
        TextView dm = (TextView) getActivity().findViewById(R.id.myStatusTextView);
        dm.setText(displayMessage);

        if(tagMessage == null) return ;
        TextView tm = (TextView) getActivity().findViewById(R.id.myTagMessage);
        tm.setText(tagMessage);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
