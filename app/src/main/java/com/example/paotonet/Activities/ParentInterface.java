package com.example.paotonet.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.paotonet.R;
import com.google.firebase.auth.FirebaseAuth;

public class ParentInterface extends AppCompatActivity implements View.OnClickListener {

    ImageView health_statement;
    ImageView messages;
    ImageView live_stream;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_interface);

        auth = FirebaseAuth.getInstance();

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
            Intent intent = new Intent(getApplicationContext(), PrivateMessages.class);
            startActivity(intent);
        }
        if (v == live_stream) {
            Intent intent = new Intent(getApplicationContext(), LiveStream.class);
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
                auth.signOut();
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
