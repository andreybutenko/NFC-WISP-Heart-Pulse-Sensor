package com.example.nfc_wisp_android_app.nfc_wisp_android_app;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;

import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import java.io.InputStream;
import java.io.PipedReader;
import java.lang.reflect.Array;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;


public class ChartUpdator extends AsyncTask<Float, float[], Float> {
    private MainActivity mMain;
    private DoMeasurementFragment fragment;
    private byte[] out;
    private String curTime;
    private int OUTPUT_SIZE = 16;
    private int INPUT_SIZE = 16;
    private static final int MAX_WIDTH = 512;
    private Tag tag;


    public ChartUpdator(MainActivity mMain, String curTime, Tag tag) {
        this.mMain = mMain;
        this.fragment = (DoMeasurementFragment) this.mMain.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        this.curTime = curTime;
        this.tag = tag;
        this.out = new byte[OUTPUT_SIZE];
    }

    @Override
    protected Float doInBackground(Float... timeStamp) {
        float time = timeStamp[0];
        IsoDep isoDep = IsoDep.get(tag);
        try {
            isoDep.connect();
            byte[] payload, rawPayload;
            ArrayList<Byte> r_array = new ArrayList<>();
            int count = 0;
            while(isoDep.isConnected() && count < MAX_WIDTH) {
                isoDep.setTimeout(5000);
                rawPayload = isoDep.transceive(out);
                payload = decodePayload(rawPayload);
                float[] buf = new float[payload.length];
                for(int j = 0; j < INPUT_SIZE; j++) {
                    r_array.add(payload[j]);
                    int f = (payload[j] & 0xFF);
                    buf[j] = (float) f;
                    time++;
                }
                float[] times = new float[]{time};
                publishProgress(buf, buf, times);
                count++;
            }
            byte[] result = new byte[r_array.size()];
            for(int i = 0; i < result.length; i++) {
                result[i] = r_array.get(i);
            }
            mMain.dbManager.onInsertMeasurement(mMain.user.uid, result, result, curTime);
            isoDep.close();
            return time;
        } catch (Exception e) {
            Snackbar.make(mMain.findViewById(android.R.id.content), "Tag is lost, please attach the tag to the cellphone.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Log.e("Debug", "Fail to send new data", e);
        }
        return null;
    }

    // decode function
    private byte[] decodePayload(byte[] rawPayload) {
        //TODO implement decode function
        return rawPayload;
    }


    @Override
    protected void onProgressUpdate(float[]... entries) {
        try {
            fragment.onDataSetChange(entries[0], entries[1], entries[2][0]);
        } catch (Exception e) {
            Log.e("Debug", "Fail to publish updates", e);
        }
    }

    @Override
    protected void onPostExecute(Float result) {
        try {
            mMain.timerCallBack(result);
            Snackbar.make(mMain.findViewById(android.R.id.content), "Measurement Finished.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } catch (Exception e) {
            Log.e("Debug", "Fail to update new time");
        }
    }
}
