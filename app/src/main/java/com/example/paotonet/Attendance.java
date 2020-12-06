package com.example.paotonet;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.paotonet.Adapters.ChildrenAdapter;
import com.example.paotonet.Objects.Child;
import com.example.paotonet.Objects.MyDate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Attendance extends AppCompatActivity implements View.OnClickListener {
    ChildrenAdapter childrenAdapter;
    ArrayList<Child> children = new ArrayList<>();
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        // initialize DB reference, listView and adapter
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("children");
        ListView listView = findViewById(R.id.listView);
        childrenAdapter = new ChildrenAdapter(Attendance.this, 0, 0, children);

        // Set listener on the children saved in the database
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get all children from the DB
                for (DataSnapshot data : dataSnapshot.getChildren())
                    children.add(data.getValue(Child.class));
                // Update the listView for any changes to display all children information
                listView.setAdapter(childrenAdapter);
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
        if (v == saveBtn) { // clicked on the save button
            // get all present children ID and create suitable database reference
            ArrayList<Integer> presentChildrenID = childrenAdapter.getPresentChildrenID();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference dbRef = database.getReference("reports");
            MyDate current = new MyDate();
            // save all the checked children ID in the database
            if(presentChildrenID.isEmpty())
                dbRef.child(current.toDateString()).setValue("No child was present", listener);
            else
                dbRef.child(current.toDateString()).setValue(presentChildrenID, listener);
        }
    }

    // Listener that inform the user if the information is stored properly in thw DB or not
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