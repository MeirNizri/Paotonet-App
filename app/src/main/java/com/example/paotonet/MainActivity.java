package com.example.paotonet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.paotonet.Objects.Parent;
import com.example.paotonet.Objects.Teacher;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        // code to insert child or message to the database
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference dbRef = database.getReference();
//        Child child = new Child(296382916, "Adi Alon", "@drawable/baby1", new MyDate(30,8,2017));
//        dbRef.child("children").child(child.getName()).setValue(child, listener);
//        Message m = new Message("Meir", "this is a test message 3", new MyDate());
//        dbRef.child("messages").child(m.toDateAndTimeString()).setValue(m, listener);
//        Teacher t = new Teacher("Gali Levi", "Gali@gmail.com", 12345678);
//        dbRef.child("teachers").child("kbJnIrsTldeX0Y3uD3pujQ6hKv62").setValue(t);
//        Parent t = new Parent("Dolev Brender", "2dolev20@gmail.com", 12345678, 328736013);
//        dbRef.child("parents").child("i3OakCgM21YbnvVM0si9afHHzB02").setValue(t);
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
}