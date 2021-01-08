package com.example.paotonet.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.paotonet.Objects.Child;
import com.example.paotonet.Objects.MyDate;
import com.example.paotonet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HealthStatement extends AppCompatActivity implements View.OnClickListener {

    EditText Child_ID, Child_Name, Child_Family_Name, Child_Birth_Day;
    EditText KinderGarten_City, Manager_Name, KinderGarten_Name;
    EditText Parent_ID, Parent_Name, Parent_Family_Name, Parent_Phone, Parent_Email;
    CheckBox CheckBox1, CheckBox2, CheckBox3, CheckBox4, CheckBox5;
    Button Submit;

    ArrayList<Child> children = new ArrayList<Child>();

    AlertDialog.Builder Failed;
    AlertDialog.Builder Success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_statement);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("children");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get all children from the DB
                for (DataSnapshot data : dataSnapshot.getChildren())
                    children.add(data.getValue(Child.class));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(null, "loadPost:onCancelled", databaseError.toException());
            }
        });

        Success = new AlertDialog.Builder(this).setTitle("Successfully Submitted!")
                .setMessage("The Details have been stored in the system")
                .setNeutralButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ResetEditTexts();
                    }
                });
        Failed = new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Submission has been failed!")
                .setMessage("Please check if the id of your child is correct and check all the checkboxes")
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ResetEditTexts();
                    }
                });

        Child_ID = (EditText)findViewById(R.id.child_id);
        Child_Name = (EditText)findViewById(R.id.child_name);
        Child_Family_Name = (EditText)findViewById(R.id.child_family_name);
        Child_Birth_Day = (EditText)findViewById(R.id.child_birthday);
        KinderGarten_City = (EditText)findViewById(R.id.city_name);
        KinderGarten_Name = (EditText)findViewById(R.id.Kindergarten_name);
        Manager_Name = (EditText)findViewById(R.id.manager);
        Parent_ID = (EditText)findViewById(R.id.parent_id);
        Parent_Name = (EditText)findViewById(R.id.parent_name);
        Parent_Family_Name = (EditText)findViewById(R.id.parent_family_name);
        Parent_Phone = (EditText)findViewById(R.id.phone_number);
        Parent_Email = (EditText)findViewById(R.id.Email_name);
        CheckBox1 = (CheckBox)findViewById(R.id.checkbox1);
        CheckBox2 = (CheckBox)findViewById(R.id.checkbox2);
        CheckBox3 = (CheckBox)findViewById(R.id.checkbox3);
        CheckBox4 = (CheckBox)findViewById(R.id.checkbox4);
        CheckBox5 = (CheckBox)findViewById(R.id.checkbox5);

        Submit = (Button)findViewById(R.id.submit_button);
        Submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == Submit) {
            if(!Child_ID.getText().toString().equals("")) {
                int input = Integer.parseInt(Child_ID.getText().toString());
                boolean isExist = false;
                Child child = null;
                boolean Checks = CheckBox1.isChecked() && CheckBox2.isChecked() && CheckBox3.isChecked()
                        && CheckBox4.isChecked() && CheckBox5.isChecked();

                for (int i = 0; i < children.size(); i++) {
                    int id = children.get(i).getId();
                    if (id == input) {
                        isExist = true;
                        child = children.get(i);
                        break;
                    }
                }
                if (isExist && Checks) {
                    Success.show();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference dbRef = database.getReference("children");
                    MyDate current = new MyDate();
                    dbRef.child(child.getName()).child("lastSignedDeclaration").setValue(current.toDateString(), listener);
                } else {
                    Failed.show();
                }
            }
            else{
                Failed.show();
            }

        }

    }

    // Listener that inform the user if the information is stored properly in thw DB or not
    DatabaseReference.CompletionListener listener = new DatabaseReference.CompletionListener() {
        @Override
        public void onComplete(DatabaseError dbError, DatabaseReference dbReference) {
            if (dbError != null)
                Toast.makeText(getApplicationContext(),dbError.getMessage(), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(),"Daily report saved",Toast.LENGTH_LONG).show();
        }
    };

    public void ResetEditTexts(){
        Child_ID.setText("");
        Child_Name.setText("");
        Child_Family_Name.setText("");
        Child_Birth_Day.setText("");
        KinderGarten_City.setText("");
        KinderGarten_Name.setText("");
        Manager_Name.setText("");
        Parent_ID.setText("");
        Parent_Name.setText("");
        Parent_Family_Name.setText("");
        Parent_Phone.setText("");
        Parent_Email.setText("");
        CheckBox1.setChecked(false);
        CheckBox2.setChecked(false);
        CheckBox3.setChecked(false);
        CheckBox4.setChecked(false);
        CheckBox5.setChecked(false);
    }
}