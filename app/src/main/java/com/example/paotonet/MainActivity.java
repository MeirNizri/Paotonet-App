package com.example.paotonet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView attendance;
    ImageView messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create views and listeners
        attendance = (ImageView) findViewById(R.id.statement_img);
        messages = (ImageView) findViewById(R.id.msg_img);
        attendance.setOnClickListener(this);
        messages.setOnClickListener(this);

        // code to insert child or message to the database
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference dbRef = database.getReference();
//        Child child = new Child(296382916, "Adi Alon", "@drawable/baby1", new MyDate(30,8,2017));
//        dbRef.child("children").child(child.getName()).setValue(child, listener);
//        Message m = new Message("Meir", "this is a test message 3", new MyDate());
//        dbRef.child("messages").child(m.toDateAndTimeString()).setValue(m, listener);
    }

    @Override
    public void onClick(View v) {
        if (v == attendance) {
            Intent intent = new Intent(getApplicationContext(), LiveCamera.class);
            startActivity(intent);
        }
        if (v == messages) {
            Intent intent = new Intent(getApplicationContext(), Messages.class);
            startActivity(intent);
        }
    }
}