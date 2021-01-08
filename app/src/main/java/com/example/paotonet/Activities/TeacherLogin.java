package com.example.paotonet.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.paotonet.Objects.Teacher;
import com.example.paotonet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TeacherLogin extends AppCompatActivity implements View.OnClickListener {
    EditText email, password;
    Button login, back;
    TextView invalid;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);

        login = (Button) findViewById(R.id.login);
        back = (Button) findViewById(R.id.back);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        invalid = (TextView) findViewById(R.id.invalid_info);
        firebaseAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == login) {
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();
            signIn(emailText, passwordText);

            if (v == back) {
                firebaseAuth.signOut();
                finish();
            }
        }
    }

    private void signIn(String email, String password) {
        if (email.length() < 1 || password.length() < 1)
            Toast.makeText(getApplicationContext(), "Please enter email address and password", Toast.LENGTH_SHORT).show();
        else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String userId = firebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users/teachers");
                        ValueEventListener listener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(userId)) {
                                    Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), TeacherInterface.class);
                                    // Get the Teacher name and kindergarten id pass it to the next intent
                                    Teacher t = dataSnapshot.child(userId).getValue(Teacher.class);
                                    intent.putExtra("userName", t.getName());
                                    intent.putExtra("kindergartenId", t.getKindergartenId());
                                    startActivity(intent);
                                } else {
                                    resetLogin();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w(null, "loadPost:onCancelled", databaseError.toException());
                            }
                        };
                        //
                        dbRef.addListenerForSingleValueEvent(listener);
                    } else
                        resetLogin();
                }
            });
        }
    }

    private void resetLogin() {
        invalid.setText("Incorrect email address or password. Please try again.");
        email.setText("");
        password.setText("");
    }
}