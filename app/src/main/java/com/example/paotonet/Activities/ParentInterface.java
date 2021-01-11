package com.example.paotonet.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.paotonet.R;

public class ParentInterface extends AppCompatActivity implements View.OnClickListener {
    FrameLayout privateMessages;
    FrameLayout generalMessages;
    FrameLayout health_statement;
    FrameLayout live_stream;

    TextView welcome_msg;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_interface);

        // Set welcome message with the first name of the user
        userName = getIntent().getExtras().getString("userName");
        welcome_msg = (TextView) findViewById(R.id.welcome_msg);
        welcome_msg.setText("Hi " + userName.substring(0, userName.indexOf(" ")));

        // initialize views and listeners
        privateMessages = (FrameLayout) findViewById(R.id.frameLayout);
        generalMessages = (FrameLayout) findViewById(R.id.frameLayout2);
        health_statement = (FrameLayout) findViewById(R.id.frameLayout3);
        live_stream = (FrameLayout) findViewById(R.id.frameLayout4);
        privateMessages.setOnClickListener(this);
        generalMessages.setOnClickListener(this);
        health_statement.setOnClickListener(this);
        live_stream.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // Get the kindergarten id passed from the previous activity
        int kindergartenId = getIntent().getExtras().getInt("kindergartenId");
        int childId = getIntent().getExtras().getInt("childId");

        if (v == privateMessages) {
            Intent intent = new Intent(getApplicationContext(), PrivateMessages.class);
            intent.putExtra("userName", userName);
            intent.putExtra("kindergartenId", kindergartenId);
            startActivity(intent);
        } else if (v == generalMessages) {
            Intent intent = new Intent(getApplicationContext(), GeneralMessages.class);
            intent.putExtra("userName", userName);
            intent.putExtra("kindergartenId", kindergartenId);
            startActivity(intent);
        } else if (v == health_statement) {
            Intent intent = new Intent(getApplicationContext(), HealthStatement.class);
            intent.putExtra("userName", userName);
            intent.putExtra("kindergartenId", kindergartenId);
            intent.putExtra("childId", childId);
            startActivity(intent);
        } else if (v == live_stream) {
            Intent intent = new Intent(getApplicationContext(), LiveStream.class);
            startActivity(intent);
        }
    }
}
