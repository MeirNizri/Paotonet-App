package com.example.paotonet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class TeacherInterface extends AppCompatActivity implements View.OnClickListener {
    ImageView attendance;
    ImageView messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_interface);

        // create views and listeners
        attendance = (ImageView) findViewById(R.id.attend_img);
        messages = (ImageView) findViewById(R.id.msg_img);
        attendance.setOnClickListener(this);
        messages.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == attendance) {
            Intent intent = new Intent(getApplicationContext(), Attendance.class);
            startActivity(intent);
        }
        if (v == messages) {
            Intent intent = new Intent(getApplicationContext(), Messages.class);
            startActivity(intent);
        }
    }
}