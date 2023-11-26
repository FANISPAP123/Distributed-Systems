package com.example.map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Activity1 extends Activity {
    Button next;
    ImageView iv;
    String name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        iv = findViewById(R.id.android1);
        next = findViewById(R.id.button1);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = findViewById(R.id.editTextName);
                name = input.getText().toString().trim();
                Intent myIntent = new Intent(view.getContext(), Activity2.class);
                myIntent.putExtra("name", name);
                startActivity(myIntent);
            }
        });
    }
}
