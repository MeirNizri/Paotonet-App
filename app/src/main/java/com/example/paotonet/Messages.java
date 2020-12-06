package com.example.paotonet;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.paotonet.Adapters.MessageAdapter;
import com.example.paotonet.Objects.Message;
import com.example.paotonet.Objects.MyDate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Messages extends AppCompatActivity implements View.OnClickListener {
    ArrayList<Message> messages = new ArrayList<>();
    EditText writeMsg;
    ImageView sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        // initialize DB reference, listView and adapter
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("messages");
        ListView listView = findViewById(R.id.messages);
        MessageAdapter messageAdapter = new MessageAdapter(Messages.this, 0, 0, messages);

        // Set listener on the database messages
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get all messages from the DB. The last message will be at the top of the array
                messages.clear();
                for (DataSnapshot data : dataSnapshot.getChildren())
                    messages.add(0, data.getValue(Message.class));
                // Update the listView for any changes to display all messages
                listView.setAdapter(messageAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(null, "loadPost:onCancelled", databaseError.toException());
            }
        });

        // create views and set listener on the send button
        writeMsg = (EditText)findViewById(R.id.write_message);
        sendBtn = (ImageView)findViewById(R.id.send_button);
        sendBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == sendBtn) { // clicked on send button
            // Get the message written
            String msg = writeMsg.getText().toString();
            if(msg.isEmpty())
                Toast.makeText(getApplicationContext(),"Empty message",Toast.LENGTH_LONG).show();
            else {
                // Enter the message to the database
                Message toSend = new Message("Meir", msg, new MyDate());
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dbRef = database.getReference().child("messages");
                dbRef.child(toSend.toDateAndTimeString()).setValue(toSend, listener);
                // Reset the EditText box
                writeMsg.setText("");
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
                Toast.makeText(getApplicationContext(),"Message sent",Toast.LENGTH_LONG).show();
        }
    };
}