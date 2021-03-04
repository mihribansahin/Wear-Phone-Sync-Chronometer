package com.mishsapp.wearossendchronometer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.io.InputStream;
import java.util.concurrent.ExecutionException;

public class MainActivity extends WearableActivity implements DataClient.OnDataChangedListener {
    private static final String COUNT_KEY = "count";
    private static final String COUNT_PATH = "/count";
    private String temp;
    private TextView textViewTime;

    public void init(){
        textViewTime = findViewById(R.id.textViewTimee);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        // Enables Always-on
        setAmbientEnabled();
    }
   @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.e("Tebrikler..", "On data changed calisti !");

        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo(COUNT_PATH) == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    temp =  String.valueOf(dataMap.getInt(COUNT_KEY));

                    Log.e("Mukemmelsin..", "veriyi aldin :)");
                }

                updateCount(temp);
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                Log.e("Maalesef..", "veri silinmis :(" );
            }
        }
    }

    private void updateCount(String c) {
        textViewTime.setText(c);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Wearable.getDataClient(this).addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.getDataClient(this).removeListener(this);
    }

}