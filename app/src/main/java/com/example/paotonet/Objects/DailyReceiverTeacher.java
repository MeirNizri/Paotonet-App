package com.example.paotonet.Objects;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.paotonet.Activities.MainActivity;
import com.example.paotonet.R;

public class DailyReceiverTeacher extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // create notification builder
        Notification.Builder notificationBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder = new Notification.Builder(context, "123");
        } else {
            notificationBuilder = new Notification.Builder(context);
        }

        //intent to activate when press on notification
        Intent landingIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingLandingIntent = PendingIntent.getActivity(context, 0, landingIntent,0);

        // create notification
        Notification notification = notificationBuilder
                .setContentTitle("Attendance Report Reminder")
                .setSmallIcon(R.drawable.reminder_icon)
                .setContentText("Do not forget to report on the present children in kindergarten")
                .setContentIntent(pendingLandingIntent).build();

        // create notification manager
        NotificationManager notificationManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        // send notification
        notificationManager.notify((int) System.currentTimeMillis(), notification);
    }
}