package com.example.map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Activity3 extends Activity {
    TextView sdistTextView, seleTextView, stimeTextView, speedTextView;
    String sdistResult, seleResult, stimeResult, sspeed,dist1,ele1,time1,speed1;
    MyThread myThread;

    Button stat;
    String name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity3);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        sdistTextView = (TextView) findViewById(R.id.s1);
        seleTextView = (TextView) findViewById(R.id.s2);
        stimeTextView = (TextView) findViewById(R.id.s3);
        speedTextView = (TextView) findViewById(R.id.s4);

        stat = (Button) findViewById(R.id.stat);

        Handler myHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                //Bundle bundle = msg.getData();
                Log.d("MyActivity", "handleMessage called");
                sdistResult = msg.getData().getString("distance");
                seleResult =  msg.getData().getString("elevation");
                stimeResult =  msg.getData().getString("time");
                sspeed =  msg.getData().getString("speed");
                dist1 = msg.getData().getString("newD");
                ele1 = msg.getData().getString("newEle");
                time1 = msg.getData().getString("newTime");
                speed1 = msg.getData().getString("newSpeed");


                Log.d("MyActivity", "Distance: " + sdistResult);
                Log.d("MyActivity", "Elevation: " + seleResult);
                Log.d("MyActivity", "Time: " + stimeResult);
                Log.d("MyActivity", "Speed: " + sspeed);


                // Ενημέρωση των TextView
                sdistTextView.setText("Distance: " + sdistResult);
                seleTextView.setText("Elevation: " + seleResult);
                stimeTextView.setText("Time: " + stimeResult);
                speedTextView.setText("Speed: " + sspeed);


                /*stat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent1 = new Intent(v.getContext(),Activity4.class);
                        myIntent1.putExtra("newD", dist1);
                        myIntent1.putExtra("newEle",ele1);
                        myIntent1.putExtra("newTime",time1);
                        myIntent1.putExtra("newSpeed",speed1);
                        startActivity(myIntent1);
                    }
                });
                */
                return true;

            }
        });
        myThread = new MyThread(name,myHandler);
        myThread.start();
    }


    @Override
    protected void onStart(){
        super.onStart();
        stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent1 = new Intent(v.getContext(),Activity4.class);
                myIntent1.putExtra("newD", dist1);
                myIntent1.putExtra("newEle",ele1);
                myIntent1.putExtra("newTime",time1);
                myIntent1.putExtra("newSpeed",speed1);
                startActivity(myIntent1);
            }
        });
    }

}
