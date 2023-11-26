package com.example.map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class Activity2 extends Activity {

    ImageView iv;
    Button next1;
    String name;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        Intent in = getIntent();
        name = in.getStringExtra("name");
        iv = (ImageView) findViewById(R.id.androids);
        next1 = (Button) findViewById(R.id.button2);
    }
    @Override
    protected void onStart(){
        super.onStart();
        next1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent1 = new Intent(view.getContext(),Activity3.class);
                myIntent1.putExtra("name", name);
                startActivity(myIntent1);
            }
        });
    }

}
