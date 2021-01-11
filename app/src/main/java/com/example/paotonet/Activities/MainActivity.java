package com.example.paotonet.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import com.example.paotonet.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button parent;
    Button teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create views and listeners
        parent = (Button) findViewById(R.id.btn_parent);
        teacher = (Button) findViewById(R.id.btn_teacher);
        parent.setOnClickListener(this);
        teacher.setOnClickListener(this);

        createChannel();
    }

    @Override
    public void onClick(View v) {
        if (v == parent) {
            Intent intent = new Intent(getApplicationContext(), ParentLogin.class);
            startActivity(intent);
        }
        if (v == teacher) {
            Intent intent = new Intent(getApplicationContext(), TeacherLogin.class);
            startActivity(intent);
        }
    }

    private void createChannel() {
        NotificationManager mNotificationManager= null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mNotificationManager = getSystemService(NotificationManager.class);
        }
        String id = "123";
        CharSequence name = "channel 1- example";
        String description = "This is the desc of channel 1 example";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(id, name, importance);
            mChannel.setDescription(description);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

}