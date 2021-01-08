package com.example.paotonet.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.paotonet.Objects.Child;
import com.example.paotonet.Objects.Children;
import com.example.paotonet.Objects.DailyReport;
import com.example.paotonet.Objects.Kindergarten;
import com.example.paotonet.Objects.Message;
import com.example.paotonet.Objects.Message_Comperator;
import com.example.paotonet.Objects.Messages;
import com.example.paotonet.Objects.MyDate;
import com.example.paotonet.Objects.Parent;
import com.example.paotonet.Objects.Reports;
import com.example.paotonet.Objects.Teacher;
import com.example.paotonet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;

import java.util.ArrayList;
import java.util.Collections;

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

        // code to insert child or message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference();
        DailyReport d = new DailyReport();
        d.addChild(312237542);
        Reports r = new Reports();
        r.addReport(d);
        dbRef.child("kindergartens").child("12345678").child("reports").setValue(r);
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
}