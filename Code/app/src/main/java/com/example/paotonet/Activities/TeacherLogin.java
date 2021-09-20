package com.example.paotonet.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.paotonet.Objects.Child;
import com.example.paotonet.Objects.Children;
import com.example.paotonet.Objects.MyDate;
import com.example.paotonet.Objects.Parent;
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
    Button login, signup;
    TextView invalid, forgetPassword;
    FirebaseAuth firebaseAuth;
    DatabaseReference dbref_;

    Dialog signUpDialog, resetDialog;
    EditText teacherName, kindergartenId, teacherEmail, teacherPassword, resetEmail;
    Button signBtn, closeBtn, resetBtn, closeResetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);

        login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        invalid = (TextView) findViewById(R.id.invalid_info);
        forgetPassword = (TextView) findViewById((R.id.forgot_password));
        firebaseAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(this);
        signup.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == login) {
            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();
            signIn(emailText, passwordText);
        }
        if (v == signup) {
            createSignUpDialog();
        }
        if (v == forgetPassword) {
            createResetDialog();
        }
        if (v == signBtn) {
            signUp();
        }
        if (v == closeBtn) {
            signUpDialog.dismiss();
        }
        if (v == resetBtn) {
            resetPassword();
        }
        if (v == closeResetBtn) {
            resetDialog.dismiss();
        }
    }

    private void signIn(String email, String password) {
        // If the email field or password is empty, send the user an appropriate message
        if (email.length() < 1 || password.length() < 1)
            Toast.makeText(getApplicationContext(), "Please enter email address and password", Toast.LENGTH_SHORT).show();
        else {
            // check if email and password is valid
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // get the user object
                        String userId = firebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users/teachers");
                        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(userId)) {
                                    // send success message to user and move to teacher interface
                                    Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), TeacherInterface.class);
                                    // Get the Teacher name and kindergarten id and pass it to the next intent
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
                        });
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

    public void createSignUpDialog() {
        // set dialog Properties
        signUpDialog = new Dialog(this);
        signUpDialog.setContentView(R.layout.teacher_signup_dialog);
        signUpDialog.setCancelable(true);
        signUpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // initialize views
        teacherName = (EditText) signUpDialog.findViewById(R.id.teacher_name);
        kindergartenId = (EditText) signUpDialog.findViewById(R.id.kindergarten_id);
        teacherEmail = (EditText) signUpDialog.findViewById(R.id.email);
        teacherPassword = (EditText) signUpDialog.findViewById(R.id.password);
        signBtn = (Button) signUpDialog.findViewById(R.id.sign_up);
        closeBtn = (Button) signUpDialog.findViewById(R.id.close);
        signBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);

        // open dialog
        signUpDialog.show();
    }

    public void createResetDialog() {
        // set dialog Properties
        resetDialog = new Dialog(this);
        resetDialog.setContentView(R.layout.reset_password);
        resetDialog.setCancelable(true);
        resetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // initialize views
        resetEmail = (EditText) resetDialog.findViewById(R.id.email);
        resetBtn = (Button) resetDialog.findViewById(R.id.reset);
        closeResetBtn = (Button) resetDialog.findViewById(R.id.close);
        resetBtn.setOnClickListener(this);
        closeResetBtn.setOnClickListener(this);

        // open dialog
        resetDialog.show();
    }

    public void resetPassword() {
        String emailText = resetEmail.getText().toString();
        firebaseAuth.sendPasswordResetEmail(emailText)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "New password send to your email", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error with your email address. \nTry again or contact app support", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void signUp() {
        // initialize views
        teacherName = (EditText) signUpDialog.findViewById(R.id.teacher_name);
        kindergartenId = (EditText) signUpDialog.findViewById(R.id.kindergarten_id);
        teacherEmail = (EditText) signUpDialog.findViewById(R.id.email);
        teacherPassword = (EditText) signUpDialog.findViewById(R.id.password);

        //get mail and password
        String mail = teacherEmail.getText().toString();
        String pass = teacherPassword.getText().toString();

        //get parent details
        String teacherName_ = teacherName.getText().toString();
        String kindergartenId_ = kindergartenId.getText().toString();

        if (pass.isEmpty() || mail.isEmpty() || teacherName_.isEmpty() || kindergartenId_.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(mail, pass).
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Teacher t = new Teacher(teacherName_, mail, Integer.parseInt(kindergartenId_));

                        // insert parent and child to database
                        dbref_ = FirebaseDatabase.getInstance().getReference();
                        dbref_.child("users").child("teachers").child(firebaseAuth.getUid()).setValue(t);

                        //show success
                        Toast.makeText(getApplicationContext(), "Success! you can now login to Paotonet", Toast.LENGTH_LONG).show();
                        signUpDialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error with your registration. \nTry again or contact app support", Toast.LENGTH_LONG).show();
                    }
                });
    }
}