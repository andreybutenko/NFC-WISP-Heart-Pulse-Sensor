package com.example.nfc_wisp_android_app.nfc_wisp_android_app;


import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * A simple {@link Fragment} subclass.
 */
public class SingleHistoryMeasurementFragment extends Fragment implements OnClickListener {

    private static final String TIME = "TIME";
    private static final float AXIS_LABEL_FONT_SIZE = 12f;


    private LineChart infraredLineChart;
    private LineChart redLineChart;
    private static final int WINDOW_SIZE = 200;
    private static final float Y_INITIAL = 1000000f;
    private static final String IR_CHART_TAG = "Infrared Light Data";
    private static final String RD_CHART_TAG = "Red Light Data";
    private static final String IR_DATA_TAG = "IR";
    private static final String RD_DATA_TAG = "RD";

    public SingleHistoryMeasurementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single_history_measurement, container, false);
        Button backButton = (Button) view.findViewById(R.id.back_to_measurement_list);
        backButton.setOnClickListener(this);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        String time = args.getString(TIME);

        MainActivity mMain = (MainActivity) getActivity();
        Cursor cursor = mMain.dbManager.getSingleMeasurementHistory(mMain.user.uid, time);

        cursor.moveToFirst();
        byte[] m_ir = cursor.getBlob(0);
        byte[] m_rd = cursor.getBlob(1);

        infraredLineChart = (LineChart) getActivity().findViewById(R.id.history_ir);
        redLineChart = (LineChart) getActivity().findViewById(R.id.history_rd);
        // Line chart general configuration
        chartConfig(infraredLineChart, IR_CHART_TAG);
        chartConfig(redLineChart, RD_CHART_TAG);

        // Actual Data Flow
        LineDataSet ir = setStaticData(m_ir, IR_DATA_TAG, Color.BLUE);
        LineDataSet rd = setStaticData(m_rd, RD_DATA_TAG, Color.RED);

        if(ir != null) {
            infraredLineChart.setData(new LineData(ir));
        } else {
            infraredLineChart.setData(new LineData(setInitialData(IR_CHART_TAG, Color.BLUE)));
        }
        if(rd != null) {
            redLineChart.setData(new LineData(rd));
        } else {
            redLineChart.setData(new LineData(setInitialData(RD_DATA_TAG, Color.RED)));
        }

        infraredLineChart.invalidate();
        redLineChart.invalidate();
    }


    private LineDataSet setStaticData(byte[] measurement, String tag, int color) {
        if(measurement == null || measurement.length == 0) return null;

        float fakeTime = 0f;

        float[] dst = new float[measurement.length / 4];
        for(int i = 0; i < measurement.length; i += 4) {
            int d = 0;
            d |= (measurement[i] & 0xFF);
            d |= ((measurement[i + 1] & 0xFF) << 8);
            d |= ((measurement[i + 2] & 0xFF) << 16);
            d |= ((measurement[i + 3] & 0xFF) << 24);
            dst[i / 4] = Float.intBitsToFloat(d);
        }


        List<Entry> dataList = new ArrayList<Entry>();


        for(int i = 0; i < dst.length; i++) {
            dataList.add(new Entry(fakeTime, dst[i]));
            fakeTime++;
        }
        LineDataSet dataSet = new LineDataSet(dataList, tag);
        dataSetConfig(dataSet, color);
        return dataSet;
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
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
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

        chart.setTouchEnabled(true); // disable touch interaction

        chart.getAxisRight().setEnabled(false); // disable the right axis
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setEnabled(true);
        yAxis.setTextColor(Color.BLACK);
        yAxis.setTextSize(AXIS_LABEL_FONT_SIZE);
        yAxis.setLabelCount(7, true);
        yAxis.setDrawGridLines(true); // disable background grid line


        XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(AXIS_LABEL_FONT_SIZE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false); // disable background grid line
        chart.invalidate();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.back_to_measurement_list) {
            toMeasurementHistory();
        }
    }



    private void toMeasurementHistory() {
        MeasurementFragment fragment = new MeasurementFragment(); // create new fragment
        // set-up arguments
        Bundle args = new Bundle();
        fragment.setArguments(args);

        // begin fragment trasaction
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, fragment); // replace the transaction with current transaction
        transaction.addToBackStack(null); // add the transaction to the back stack so the user can navigate back

        transaction.commit();
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
