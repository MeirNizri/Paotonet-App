package com.example.paotonet.Activities;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.paotonet.Objects.DailyReceiverParent;
import com.example.paotonet.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class ParentInterface extends AppCompatActivity implements View.OnClickListener {
    FrameLayout privateMessages;
    FrameLayout generalMessages;
    FrameLayout health_statement;
    FrameLayout live_stream;

    TextView welcome_msg;
    String userName;

    Dialog infoDialog;
    Button cancelBtn;
    Dialog notificationDialog;
    TimePicker timePicker;
    Button setAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_interface);

        // Set welcome message with the first name of the user
        userName = getIntent().getExtras().getString("userName");
        welcome_msg = (TextView) findViewById(R.id.welcome_msg);
        welcome_msg.setText("Hi " + userName.substring(0, userName.indexOf(" ")));

        // initialize views and listeners
        privateMessages = (FrameLayout) findViewById(R.id.frameLayout);
        generalMessages = (FrameLayout) findViewById(R.id.frameLayout2);
        health_statement = (FrameLayout) findViewById(R.id.frameLayout3);
        live_stream = (FrameLayout) findViewById(R.id.frameLayout4);
        privateMessages.setOnClickListener(this);
        generalMessages.setOnClickListener(this);
        health_statement.setOnClickListener(this);
        live_stream.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // Get the kindergarten id passed from the previous activity
        int kindergartenId = getIntent().getExtras().getInt("kindergartenId");
        int childId = getIntent().getExtras().getInt("childId");

        if (v == privateMessages) {
            Intent intent = new Intent(getApplicationContext(), PrivateMessages.class);
            intent.putExtra("userName", userName);
            intent.putExtra("kindergartenId", kindergartenId);
            startActivity(intent);
        } else if (v == generalMessages) {
            Intent intent = new Intent(getApplicationContext(), GeneralMessages.class);
            intent.putExtra("userType", "parent");
            intent.putExtra("userName", userName);
            intent.putExtra("kindergartenId", kindergartenId);
            startActivity(intent);
        } else if (v == health_statement) {
            Intent intent = new Intent(getApplicationContext(), HealthStatement.class);
            intent.putExtra("userName", userName);
            intent.putExtra("kindergartenId", kindergartenId);
            intent.putExtra("childId", childId);
            startActivity(intent);
        } else if (v == live_stream) {
            Intent intent = new Intent(getApplicationContext(), LiveStream.class);
            startActivity(intent);
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
