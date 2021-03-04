package com.mishsapp.wearossendchronometer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends AppCompatActivity implements DataClient.OnDataChangedListener {

        private static final String COUNT_KEY = "count";
        private static final String COUNT_PATH = "/count";
        private Button buttonStart, buttonStop, buttonReset;
        private TextView textViewTime;
        int x;
        Runnable runnable;
        Handler handler = new Handler();

        public void init(){
            textViewTime =  findViewById(R.id.textViewTimee);
            buttonStart = findViewById(R.id.buttonStart);
            buttonStop = findViewById(R.id.buttonStop);
            buttonReset = findViewById(R.id.buttonReset);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            init();

            
            Start();
            Stop();
            Reset();

        }


    private void increaseCounter(int c) {

        PutDataMapRequest putDataMapReq = PutDataMapRequest.create(COUNT_PATH);
        putDataMapReq.getDataMap().putInt(COUNT_KEY, c);
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        Task<DataItem> putDataTask = Wearable.getDataClient(this).putDataItem(putDataReq);

        putDataTask.addOnSuccessListener(new OnSuccessListener<DataItem>() {
            @Override
            public void onSuccess(DataItem dataItem) {
                Log.e("INANILMAZSIN... ", "Veri Basarıyla gönderildi !");
            }
        });
    }


        private void Start(){

            buttonStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonStart.setEnabled(false);
                    buttonStop.setEnabled(true);
                    buttonStart.setText("BAŞLA");

                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            x++;
                            textViewTime.setText(Integer.toString(x));
                            Log.e("SAYAC" , String.valueOf(x));
                            handler.postDelayed(runnable, 1000);//her 1 saniye

                            increaseCounter(x);
                        }
                    };

                    handler.post(runnable);
                }
            });
        }

        private void Stop(){

            buttonStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonStart.setText("DEVAM");
                    buttonStart.setEnabled(true);
                    buttonStop.setEnabled(false);
                    handler.removeCallbacks(runnable);

                    increaseCounter(x);
                }
            });
        }

        private void Reset(){

            buttonReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonStart.setText("BAŞLA");
                    buttonStop.setEnabled(true);
                    buttonStart.setEnabled(true);
                    handler.removeCallbacks(runnable);
                    x=0;
                    textViewTime.setText(Integer.toString(x));

                    increaseCounter(x);
                }
            });
        }


    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {

    }

}
