package com.example.paotonet.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.paotonet.Objects.Child;
import com.example.paotonet.Objects.Children;
import com.example.paotonet.Objects.MyDate;
import com.example.paotonet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    }

    // Listener that inform the user if the information is stored properly in the DB or not
    DatabaseReference.CompletionListener listener = new DatabaseReference.CompletionListener() {
        @Override
        public void onComplete(DatabaseError dbError, DatabaseReference dbReference) {
            if (dbError != null)
                Toast.makeText(getApplicationContext(),dbError.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}