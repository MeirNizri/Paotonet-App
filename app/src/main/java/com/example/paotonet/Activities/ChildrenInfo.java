package com.example.paotonet.Activities;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.paotonet.Adapters.ChildrenInfoAdapter;
import com.example.paotonet.Objects.Child;
import com.example.paotonet.Objects.Children;
import com.example.paotonet.Objects.DailyReceiverTeacher;
import com.example.paotonet.Objects.Kindergarten;
import com.example.paotonet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class ChildrenInfo extends AppCompatActivity implements View.OnClickListener {
    int kindergartenId;

    Kindergarten kindergarten = new Kindergarten();
    Children allChildren = new Children();
    ArrayList<Child> children = new ArrayList<Child>();
    ListView listView;
    ChildrenInfoAdapter adapter;

    Button returnBtn;

    Dialog infoDialog;
    Button cancelBtn;
    Dialog notificationDialog;
    TimePicker timePicker;
    Button setAlarm;

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
                children.clear();
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
        else if(v == cancelBtn) {
            infoDialog.dismiss();
        }
        else if(v == setAlarm) {
            notificationDialog.dismiss();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                setAlarm(hour,minute);
                Toast.makeText(getApplicationContext(), "Alarm set in "+String.format("%02d:%02d", hour, minute), Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Set notification failed", Toast.LENGTH_LONG).show();
            }
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                FirebaseAuth.getInstance().signOut();
                this.finish();
                return true;
            case R.id.information:
                createInfoDialog();
                return true;
            case R.id.notification:
                createNotificationDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void createInfoDialog(){
        // set dialog Properties
        infoDialog = new Dialog(this);
        infoDialog.setContentView(R.layout.info_dialog);
        infoDialog.setCancelable(true);
        infoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // initialize views
        cancelBtn = (Button)infoDialog.findViewById(R.id.return_btn);
        cancelBtn.setOnClickListener(this);

        // open dialog
        infoDialog.show();
    }

    public void createNotificationDialog(){
        // set dialog Properties
        notificationDialog = new Dialog(this);
        notificationDialog.setContentView(R.layout.set_notification_dialog);
        notificationDialog.setCancelable(true);
        notificationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // change headline
        TextView headline = (TextView)notificationDialog.findViewById((R.id.headline));
        headline.setText("Select a time when you will be sent a reminder to fill out attendance report");

        // initialize views
        timePicker = (TimePicker)notificationDialog.findViewById((R.id.timePicker));
        setAlarm = (Button)notificationDialog.findViewById(R.id.buttonAlarm);
        setAlarm.setOnClickListener(this);

        // open dialog
        notificationDialog.show();
    }

    public void setAlarm(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Intent myIntent = new Intent(this, DailyReceiverTeacher.class);
        int ALARM_ID = 1000;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), ALARM_ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}