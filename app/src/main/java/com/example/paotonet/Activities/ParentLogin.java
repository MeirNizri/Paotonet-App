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
import com.example.paotonet.Objects.Kindergarten;
import com.example.paotonet.Objects.MyDate;
import com.example.paotonet.Objects.Parent;
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

public class ParentLogin extends AppCompatActivity implements View.OnClickListener {
    EditText email, password;
    Button login, signup;
    TextView invalid, forgetPassword;
    FirebaseAuth firebaseAuth;
    DatabaseReference dbref_;
    Children allChildren = new Children();

    Dialog signUpDialog, resetDialog;
    EditText parentName, kindergartenId, phone, parentEmail, childName, childId, moreInfo, parentPassword, resetEmail;
    Button signBtn, closeBtn, resetBtn, closeResetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);

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
                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users/parents");
                        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(userId)) {
                                    // send success message to user and move to parent interface
                                    Toast.makeText(getApplicationContext(), "Login successfully", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), ParentInterface.class);
                                    // Get the parent name, child id and kindergarten id and pass it to the next intent
                                    Parent p = dataSnapshot.child(userId).getValue(Parent.class);
                                    intent.putExtra("userName", p.getName());
                                    intent.putExtra("childId", p.getChildId());
                                    intent.putExtra("kindergartenId", p.getKindergartenId());
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
                        // if the email and password are not valid
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
        signUpDialog.setContentView(R.layout.parent_signup_dialog);
        signUpDialog.setCancelable(true);
        signUpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // initialize views
        parentName = (EditText) signUpDialog.findViewById(R.id.parent_name);
        kindergartenId = (EditText) signUpDialog.findViewById(R.id.kindergarten_id);
        phone = (EditText) signUpDialog.findViewById(R.id.phone);
        parentEmail = (EditText) signUpDialog.findViewById(R.id.email);
        parentPassword = (EditText) signUpDialog.findViewById(R.id.password);
        childName = (EditText) signUpDialog.findViewById(R.id.child_name);
        childId = (EditText) signUpDialog.findViewById(R.id.child_id);
        moreInfo = (EditText) signUpDialog.findViewById(R.id.more_info);
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
                            resetDialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error with your email address. \nTry again or contact app support", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void signUp() {
        //get mail and password
        String mail = parentEmail.getText().toString();
        String pass = parentPassword.getText().toString();

        //get parent details
        String parentName_ = parentName.getText().toString();
        String kindergartenId_ = kindergartenId.getText().toString();
        String phone_ = phone.getText().toString();

        //get child details
        String childId_ = childId.getText().toString();
        String childName_ = childName.getText().toString();
        String moreInfo_ = moreInfo.getText().toString();

        if (pass.isEmpty() || mail.isEmpty() || parentName_.isEmpty() || kindergartenId_.isEmpty() || childId_.isEmpty() || childName_.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(mail, pass).
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        MyDate current = new MyDate();
                        Parent p = new Parent(parentName_, phone_, Integer.parseInt(kindergartenId_), Integer.parseInt(childId_));
                        Child c = new Child(Integer.parseInt(childId_), childName_, "default_img", current.toDateString(), current, moreInfo_);

                        // insert parent and child to database
                        dbref_ = FirebaseDatabase.getInstance().getReference();
                        dbref_.child("users").child("parents").child(firebaseAuth.getUid()).setValue(p);
                        dbref_.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Get all children from the DB and set adapter
                                allChildren = dataSnapshot.child("kindergartens").child(kindergartenId_).child("children").getValue(Children.class);
                                allChildren.addChild(c);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w(null, "loadPost:onCancelled", databaseError.toException());
                            }
                        });
                        dbref_.child("kindergartens").child(kindergartenId_).child("children").setValue(allChildren);

                        //show success
                        Toast.makeText(getApplicationContext(), "Success! you can now login to Paotonet", Toast.LENGTH_LONG).show();
                        signUpDialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error with your registration. \nTry again or contact app support", Toast.LENGTH_LONG).show();
                    }
                });
    }
}