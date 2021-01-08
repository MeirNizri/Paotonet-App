package com.example.paotonet.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paotonet.R;
import com.google.firebase.auth.FirebaseAuth;

public class TeacherInterface extends AppCompatActivity implements View.OnClickListener {
    FrameLayout privateMessages;
    FrameLayout generalMessages;
    FrameLayout attendance;

    TextView welcome_msg;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_interface);

        // Set welcome message with the first name of the user
        userName = getIntent().getExtras().getString("userName");
        welcome_msg = (TextView)findViewById(R.id.welcome_msg);
        welcome_msg.setText("Hi " + userName.substring(0,userName.indexOf(" ")));

        // initialize views and listeners
        privateMessages = (FrameLayout) findViewById(R.id.frameLayout);
        generalMessages = (FrameLayout) findViewById(R.id.frameLayout2);
        attendance = (FrameLayout) findViewById(R.id.frameLayout3);
        privateMessages.setOnClickListener(this);
        generalMessages.setOnClickListener(this);
        attendance.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // Get the kindergarten id passed from the previous activity
        int kindergartenId = getIntent().getExtras().getInt("kindergartenId");

        if (v == privateMessages) {
            Intent intent = new Intent(getApplicationContext(), PrivateMessages.class);
            intent.putExtra("userName", userName);
            intent.putExtra("kindergartenId", kindergartenId);
            startActivity(intent);
        }
        else if (v == generalMessages) {
            Intent intent = new Intent(getApplicationContext(), GeneralMessages.class);
            intent.putExtra("userName", userName);
            intent.putExtra("kindergartenId", kindergartenId);
            startActivity(intent);
        }
        else if (v == attendance) {
            Intent intent = new Intent(getApplicationContext(), Attendance.class);
            intent.putExtra("userName", userName);
            intent.putExtra("kindergartenId", kindergartenId);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case R.id.about:
                //about
                return true;
            case R.id.notification:
                //notification();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}