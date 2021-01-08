package com.example.paotonet.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.paotonet.Adapters.ChildrenAdapter;
import com.example.paotonet.Objects.Child;
import com.example.paotonet.Objects.Children;
import com.example.paotonet.Objects.DailyReport;
import com.example.paotonet.Objects.MyDate;
import com.example.paotonet.Objects.Reports;
import com.example.paotonet.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Attendance extends AppCompatActivity implements View.OnClickListener {
    String kindergartenId;

    Children allChildren = new Children();
    Reports allReports = new Reports();
    ArrayList<Child> children = new ArrayList<Child>();
    ListView listView;
    ChildrenAdapter adapter;

    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        // find database reference to the kindergarten children
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        kindergartenId = String.valueOf(getIntent().getExtras().getInt("kindergartenId"));
        DatabaseReference dbRef = database.getReference("kindergartens/" + kindergartenId + "/children");

        // initialize listView and adapter
        listView = findViewById(R.id.listView);
        adapter = new ChildrenAdapter(Attendance.this, 0, 0, children);

        // Set listener on the children saved in the database
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get all children from the DB and set adapter
                allChildren = dataSnapshot.getValue(Children.class);
                children.addAll(allChildren.getChildren());
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(null, "loadPost:onCancelled", databaseError.toException());
            }
        });

        // Set listener on the save button
        saveBtn = (Button)findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == saveBtn) {
            createDailyReport();
        }
    }

    public void createDailyReport() {
        // create DailyReport object containing all present children ID
        ArrayList<Integer> presentChildrenID = adapter.getPresentChildrenID();
        DailyReport report = new DailyReport(presentChildrenID);

        // find database reference to the kindergarten reports
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbR = db.getReference("kindergartens/" + kindergartenId + "/reports");

        // Get all reports from the DB
        dbR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allReports = dataSnapshot.getValue(Reports.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(null, "loadPost:onCancelled", databaseError.toException());
            }
        });

        // add reports to DB
        allReports.addReport(report);
        dbR.setValue(allReports, listener);
    }

    // Listener that inform the user if the information is stored properly in the DB or not
    DatabaseReference.CompletionListener listener = new DatabaseReference.CompletionListener() {
        @Override
        public void onComplete(DatabaseError dbError, DatabaseReference dbReference) {
            if (dbError != null)
                Toast.makeText(getApplicationContext(),dbError.getMessage(), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(),"Daily report saved",Toast.LENGTH_LONG).show();
        }
    };
}