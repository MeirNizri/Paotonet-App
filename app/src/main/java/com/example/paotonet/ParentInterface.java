package com.example.paotonet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ParentInterface extends AppCompatActivity implements View.OnClickListener {

    ImageView health_statement;
    ImageView messages;
    ImageView live_stream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_interface);

        // create views and listeners
        health_statement = findViewById(R.id.form_img);
        messages = findViewById(R.id.msg_img);
        live_stream = findViewById(R.id.camera_img);
        health_statement.setOnClickListener(this);
        messages.setOnClickListener(this);
        live_stream.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == health_statement) {
            Intent intent = new Intent(getApplicationContext(), HealthStatement.class);
            startActivity(intent);
        }
        if (v == messages) {
            Intent intent = new Intent(getApplicationContext(), Messages.class);
            startActivity(intent);
        }
        if (v == live_stream) {
            Intent intent = new Intent(getApplicationContext(), LiveStream.class);
            startActivity(intent);
        }
    }
}
