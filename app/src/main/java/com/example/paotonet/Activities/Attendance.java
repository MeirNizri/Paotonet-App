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

import com.example.paotonet.Adapters.ChildrenAttendanceAdapter;
import com.example.paotonet.Objects.Child;
import com.example.paotonet.Objects.Children;
import com.example.paotonet.Objects.DailyReceiverTeacher;
import com.example.paotonet.Objects.DailyReport;
import com.example.paotonet.Objects.MyDate;
import com.example.paotonet.Objects.Reports;
import com.example.paotonet.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Attendance extends AppCompatActivity implements View.OnClickListener {
    String kindergartenId;

    Children allChildren = new Children();
    Reports allReports = new Reports();
    ArrayList<Child> children = new ArrayList<Child>();
    ListView listView;
    ChildrenAttendanceAdapter adapter;

    Button saveBtn;

    Dialog infoDialog;
    Button cancelBtn;
    Dialog notificationDialog;
    TimePicker timePicker;
    Button setAlarm;

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
        adapter = new ChildrenAttendanceAdapter(Attendance.this, 0, 0, children);

        // Set listener on the children saved in the database
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get all children from the DB and set adapter
                allChildren = dataSnapshot.getValue(Children.class);
                children.clear();
                children.addAll(allChildren.getChildren());
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(null, "loadPost:onCancelled", databaseError.toException());
            }
        });

        // set headline to current date
        TextView headline = (TextView) findViewById(R.id.headline);
        MyDate currentDate = new MyDate();
        headline.setText("Attendance Report for date " + currentDate.toDateString());

        // Set listener on the save button
        saveBtn = (Button)findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == saveBtn) {
            createDailyReport();
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