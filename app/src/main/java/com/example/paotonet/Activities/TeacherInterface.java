package com.example.paotonet.Activities;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.paotonet.R;
import com.google.firebase.auth.FirebaseAuth;

public class TeacherInterface extends AppCompatActivity implements View.OnClickListener {
    FrameLayout privateMessages;
    FrameLayout generalMessages;
    FrameLayout attendance;
    FrameLayout childrenInfo;

    TextView welcome_msg;
    String userName;

    Dialog newMsgDialog;
    Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_interface);

        // Set welcome message with the first name of the user
        userName = getIntent().getExtras().getString("userName");
        welcome_msg = (TextView) findViewById(R.id.welcome_msg);
        welcome_msg.setText("Hi " + userName.substring(0, userName.indexOf(" ")));

        // initialize views and listeners
        privateMessages = (FrameLayout) findViewById(R.id.frameLayout);
        generalMessages = (FrameLayout) findViewById(R.id.frameLayout2);
        attendance = (FrameLayout) findViewById(R.id.frameLayout3);
        childrenInfo = (FrameLayout) findViewById(R.id.frameLayout4);
        privateMessages.setOnClickListener(this);
        generalMessages.setOnClickListener(this);
        attendance.setOnClickListener(this);
        childrenInfo.setOnClickListener(this);
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
        } else if (v == generalMessages) {
            Intent intent = new Intent(getApplicationContext(), GeneralMessages.class);
            intent.putExtra("userName", userName);
            intent.putExtra("kindergartenId", kindergartenId);
            startActivity(intent);
        } else if (v == attendance) {
            Intent intent = new Intent(getApplicationContext(), Attendance.class);
            intent.putExtra("userName", userName);
            intent.putExtra("kindergartenId", kindergartenId);
            startActivity(intent);
        } else if (v == childrenInfo) {
            Intent intent = new Intent(getApplicationContext(), ChildrenInfo.class);
            intent.putExtra("userName", userName);
            intent.putExtra("kindergartenId", kindergartenId);
            startActivity(intent);
        }
        else if(v == cancelBtn) {
            newMsgDialog.dismiss();
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
                createDialog();
                return true;

            case R.id.notification:
                addNotification("123");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void createDialog(){
        // set dialog Properties
        newMsgDialog = new Dialog(this);
        newMsgDialog.setContentView(R.layout.info_dialog);
        newMsgDialog.setCancelable(true);
        newMsgDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // initialize views
        cancelBtn = (Button)newMsgDialog.findViewById(R.id.return_btn);
        cancelBtn.setOnClickListener(this);

        // open dialog
        newMsgDialog.show();
    }

    private void addNotification(String channel) {
        Notification.Builder notificationBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder = new Notification.Builder(this, channel);
        } else {
            //noinspection deprecation
            notificationBuilder = new Notification.Builder(this);
        }
        Notification notification = notificationBuilder
                .setContentTitle("notification type " + channel)
                .setSmallIcon(R.drawable.alarm_icon)
                .setContentText("You have a new message in channel " + channel).build();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), notification);
        //notificationManager.notify(1,notification)
    }
}