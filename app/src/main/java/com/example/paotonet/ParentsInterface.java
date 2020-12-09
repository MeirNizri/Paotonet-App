package com.example.paotonet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ParentsInterface extends AppCompatActivity implements View.OnClickListener{

    ImageView camera;
    ImageView messages;
    ImageView statement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_interface);
        // create views and listeners
        camera = (ImageView) findViewById(R.id.camera_img);
        messages = (ImageView) findViewById(R.id.msg_img);
        statement = (ImageView) findViewById(R.id.statement_img);
        camera.setOnClickListener(this);
        messages.setOnClickListener(this);
        statement.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == camera) {
            Intent intent = new Intent(getApplicationContext(), LiveCamera.class);
            startActivity(intent);
        }
        if (v == statement) {
            Intent intent = new Intent(getApplicationContext(), HealthStatement.class);
            startActivity(intent);
        }
        if (v == messages) {
            Intent intent = new Intent(getApplicationContext(), Messages.class);
            startActivity(intent);
        }
    }
}