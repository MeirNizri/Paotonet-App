package com.example.paotonet.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import com.example.paotonet.Objects.Child;
import com.example.paotonet.Objects.Children;
import com.example.paotonet.Objects.DailyReport;
import com.example.paotonet.Objects.Kindergarten;
import com.example.paotonet.Objects.Message;
import com.example.paotonet.Objects.Messages;
import com.example.paotonet.Objects.MyDate;
import com.example.paotonet.Objects.Reports;
import com.example.paotonet.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

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

        createChannel();

//        MyDate myDate = new MyDate();
//        Child c1 = new Child(312237542, "Zur Nizri", "zur", myDate.toDateString(), myDate, "Lectose intolarence");
//        Child c2 = new Child(318322542, "Adi Alon", "adi", myDate.toDateString(), myDate, "");
//        Child c3 = new Child(312237563, "Harel Nizri", "harel", myDate.toDateString(), myDate, "");
//        Child c4 = new Child(296382916, "Ido Lev", "ido", myDate.toDateString(), myDate, "");
//        Child c5 = new Child(328736013, "Shaked Cohen", "shaked", myDate.toDateString(), myDate, "");
//        Child c6 = new Child(278352671, "Tamar Haviv", "tamar", myDate.toDateString(), myDate, "");
//        Children c = new Children();
//        Message m0 = new Message("test", "Gali Levi", "Meir Nizri", "test", myDate);
//        Messages m = new Messages();
//        m.addMessage(m0);
//        ArrayList<Integer> a = new ArrayList<>();
//        DailyReport d = new DailyReport(a);
//        Reports r = new Reports();
//        r.addReport(d);
//        c.addChild(c1);c.addChild(c2);c.addChild(c3);c.addChild(c4);c.addChild(c5);c.addChild(c6);
//        Kindergarten k = new Kindergarten(12345678, "Otcharot", "054-9764539", "Sarit Biton", "Bracha, Kashti 4", c,m,r);

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference dbref = database.getReference("kindergartens/12345678");
//        dbref.child("children").setValue(c);
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

    private void createChannel() {
        NotificationManager mNotificationManager= null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mNotificationManager = getSystemService(NotificationManager.class);
        }
        String id = "123";
        CharSequence name = "channel 1- example";
        String description = "This is the desc of channel 1 example";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(id, name, importance);
            mChannel.setDescription(description);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

}