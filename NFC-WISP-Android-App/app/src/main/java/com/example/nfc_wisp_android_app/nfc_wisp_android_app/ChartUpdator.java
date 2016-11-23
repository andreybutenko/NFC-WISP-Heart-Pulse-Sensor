package com.example.nfc_wisp_android_app.nfc_wisp_android_app;

import com.github.mikephil.charting.data.Entry;

import android.nfc.Tag;
import android.os.AsyncTask;
import android.util.Log;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;


public class ChartUpdator extends AsyncTask<Float, Entry, Float> {
    private MainActivity mMain;
    private DoMeasurementFragment fragment;
    private Tag tag;


    private InputStream ir_in;
    private InputStream rd_in;


    public ChartUpdator(MainActivity mMain, Tag tag) {
        this.mMain = mMain;
        this.tag = tag;
        this.fragment = (DoMeasurementFragment) this.mMain.getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        // sample data
        this.ir_in = this.mMain.getResources().openRawResource(R.raw.vashnavi_ir);
        this.rd_in = this.mMain.getResources().openRawResource(R.raw.vashnavi_rd);
    }

    @Override
    protected Float doInBackground(Float... timeStamp) {
        try {
            float time = timeStamp[0];
            Scanner ir = new Scanner(ir_in);
            Scanner rd = new Scanner(rd_in);


            ArrayList<Integer> ir_list = new ArrayList<>();
            ArrayList<Integer> rd_list = new ArrayList<>();

            while(ir.hasNext() && rd.hasNext()) {
                float ir_d = Float.parseFloat(ir.next());
                float rd_d = Float.parseFloat(rd.next());


                int ir_bit = Float.floatToIntBits(ir_d);
                int rd_bit = Float.floatToIntBits(rd_d);
                ir_list.add(ir_bit);
                rd_list.add(rd_bit);

                updateChart(ir_d, rd_d, time);

                time++;
            }
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String curTime = sdf.format(c.getTime());

            byte[] ir_ba = toByteArray(ir_list);
            byte[] rd_ba = toByteArray(rd_list);

            mMain.dbManager.onInsertMeasurement(mMain.user.uid, ir_ba, rd_ba, curTime);
            return time;
        } catch (Exception e) {
            Log.e("Debug", "Fail to send new data", e);
        }
        return null;
    }


    private byte[] toByteArray(ArrayList<Integer> list) {
        if(list == null || list.size() == 0) return null;
        byte[] ba = new byte[4 * list.size()];
        for(int i = 0; i < list.size(); i++) {
            int bitRep = list.get(i);
            ba[4 * i] = (byte) (bitRep & 0xFF);
            ba[4 * i + 1] = (byte) ((bitRep >> 8) & 0xFF);
            ba[4 * i + 2] = (byte) ((bitRep >> 16) & 0xFF);
            ba[4 * i + 3] = (byte) ((bitRep >> 24) & 0xFF);
        }
        return ba;
    }

    private void updateChart(float ir_d, float rd_d, float time) {
        Entry ir_entry = new Entry(time, ir_d);
        Entry rd_entry = new Entry(time, rd_d);
        publishProgress(ir_entry, rd_entry);
    }

    @Override
    protected void onProgressUpdate(Entry... entries) {
        try {
            fragment.onDataSetChange(entries[0], entries[1]);
        } catch (Exception e) {
            Log.e("Debug", "Fail to publish updates", e);
        }
    }

    @Override
    protected void onPostExecute(Float result) {
        try {
            mMain.timerCallBack(result);
        } catch (NullPointerException e) {
            Log.e("Debug", "Fail to update new time");
        }
    }
}
