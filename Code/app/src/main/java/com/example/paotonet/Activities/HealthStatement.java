package com.example.paotonet.Activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.paotonet.Objects.Children;
import com.example.paotonet.Objects.DailyReceiverParent;
import com.example.paotonet.Objects.MyDate;
import com.example.paotonet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class HealthStatement extends AppCompatActivity implements View.OnClickListener {
    CheckBox[] checkBoxes = new CheckBox[5];
    Button confirm;
    TextView headline;
    TextView declaration_details;

    DatabaseReference dbRef;

    int kindergartenId;
    int childId;
    Children allChildren = new Children();
    MyDate currentDate = new MyDate();

    AlertDialog.Builder Failed;
    AlertDialog.Builder Success;

    Dialog infoDialog;
    Button cancelBtn;
    Dialog notificationDialog;
    TimePicker timePicker;
    Button setAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_statement);

        // Get all children from the DB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        kindergartenId = getIntent().getExtras().getInt("kindergartenId");
        dbRef = database.getReference("kindergartens/" + kindergartenId + "/children");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allChildren = dataSnapshot.getValue(Children.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(null, "loadPost:onCancelled", databaseError.toException());
            }
        });

        // set date, child id and kindergarten id in the headline of this activity
        headline = (TextView) findViewById(R.id.headline);
        declaration_details = (TextView) findViewById(R.id.declaration_details);
        childId = getIntent().getExtras().getInt("childId");
        headline.setText("Health Declaration for date " + currentDate.toDateString());
        declaration_details.setText("child id: " + childId + ",  Kindergarten Id: " + kindergartenId);

        // initialize all checkboxes and confirm button
        checkBoxes[0] = (CheckBox) findViewById(R.id.checkbox0);
        checkBoxes[1] = (CheckBox) findViewById(R.id.checkbox1);
        checkBoxes[2] = (CheckBox) findViewById(R.id.checkbox2);
        checkBoxes[3] = (CheckBox) findViewById(R.id.checkbox3);
        checkBoxes[4] = (CheckBox) findViewById(R.id.checkbox4);
        confirm = (Button) findViewById(R.id.confirm_button);
        confirm.setOnClickListener(this);

        // create dialog object for successful submission and failed submission
        Success = new AlertDialog.Builder(this).setTitle("Successfully Submitted!")
                .setMessage("The declaration has been saved in the system")
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        Failed = new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Submission failed!")
                .setMessage("You must check each section to confirm the health declaration")
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == confirm) {
            boolean allChecked = true;
            for (int i = 0; i < checkBoxes.length; i++)
                allChecked = allChecked && checkBoxes[i].isChecked();

            if (allChecked) {
                Success.show();
                allChildren.updateHealthDeclaration(childId);
                dbRef.setValue(allChildren, listener);
            } else {
                Failed.show();
            }
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

    // Listener that inform the user if the information is stored properly in the DB or not
    DatabaseReference.CompletionListener listener = new DatabaseReference.CompletionListener() {
        @Override
        public void onComplete(DatabaseError dbError, DatabaseReference dbReference) {
            if (dbError != null)
                Toast.makeText(getApplicationContext(),dbError.getMessage(), Toast.LENGTH_LONG).show();
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

        Intent myIntent = new Intent(this, DailyReceiverParent.class);
        int ALARM_ID = 1000;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), ALARM_ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}