package com.example.paotonet.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.paotonet.Adapters.ChildrenInfoAdapter;
import com.example.paotonet.Objects.Child;
import com.example.paotonet.Objects.Children;
import com.example.paotonet.Objects.Kindergarten;
import com.example.paotonet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChildrenInfo extends AppCompatActivity implements View.OnClickListener {
    int kindergartenId;

    Kindergarten kindergarten = new Kindergarten();
    Children allChildren = new Children();
    ArrayList<Child> children = new ArrayList<Child>();
    ListView listView;
    ChildrenInfoAdapter adapter;

    Button returnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_info);

        // find database reference to the user kindergarten
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        kindergartenId = getIntent().getExtras().getInt("kindergartenId");
        DatabaseReference dbRef = database.getReference("kindergartens/" + kindergartenId);

        // initialize listView and adapter
        listView = findViewById(R.id.children_info);
        adapter = new ChildrenInfoAdapter(ChildrenInfo.this, 0, 0, children);
        adapter.getAllParents(kindergartenId);

        // Set listener on the children saved in the database
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get kindergarten info and set all kindergarten fields
                kindergarten = dataSnapshot.getValue(Kindergarten.class);
                setKindergartenInfo(kindergarten);

                // Get all children from the DB and set adapter
                allChildren = dataSnapshot.child("children").getValue(Children.class);
                children.addAll(allChildren.getChildren());
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(null, "loadPost:onCancelled", databaseError.toException());
            }
        });

        // Set listener on the save button
        returnBtn = (Button) findViewById(R.id.return_btn);
        returnBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == returnBtn) {
            finish();
        }
    }

    //
    public void setKindergartenInfo(Kindergarten k) {
        TextView name = (TextView) findViewById(R.id.name);
        TextView id = (TextView) findViewById(R.id.id);
        TextView manager = (TextView) findViewById(R.id.manager);
        TextView phone = (TextView) findViewById(R.id.phone);
        TextView address = (TextView) findViewById(R.id.address);
        name.setText("Name: "+k.getName());
        id.setText("Id: "+k.getId());
        manager.setText("Manager: "+k.getManager());
        phone.setText("Phone: "+k.getPhone());
        address.setText("Address: "+k.getAddress());
    }
}