package com.example.paotonet.Activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.paotonet.Adapters.MessageAdapter;
import com.example.paotonet.Objects.Message;
import com.example.paotonet.Objects.Message_Comperator;
import com.example.paotonet.Objects.Messages;
import com.example.paotonet.Objects.MyDate;
import com.example.paotonet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class PrivateMessages extends AppCompatActivity implements View.OnClickListener {
    String userName;
    String kindergartenId;

    ArrayList<Message> sendMessages = new ArrayList<Message>();
    ArrayList<Message> receivedMessages = new ArrayList<Message>();
    Button send;
    Button received;
    Button newMsg;

    DatabaseReference dbRef;
    ListView listView;
    MessageAdapter sendMessageAdapter;
    MessageAdapter receivedMessageAdapter;
    Messages allMessages;
    static boolean sendMessageMode;

    Dialog newMsgDialog;
    EditText destination;
    EditText subject;
    EditText content;
    Button sendBtn;
    Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_messages);

        // find database reference to the kindergarten messages
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        kindergartenId = String.valueOf(getIntent().getExtras().getInt("kindergartenId"));
        dbRef = database.getReference("kindergartens/" + kindergartenId + "/messages");

        // initialize listView and adapters
        listView = findViewById(R.id.messages);
        sendMessageAdapter = new MessageAdapter(PrivateMessages.this, 0, 0, sendMessages);
        receivedMessageAdapter = new MessageAdapter(PrivateMessages.this, 0, 0, receivedMessages);

        // Set listener on the database to extract all user messages
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sendMessages.clear();
                receivedMessages.clear();

                // Get all messages from the DB
                allMessages = dataSnapshot.getValue(Messages.class);
                userName = getIntent().getExtras().getString("userName");
                sendMessages.addAll(allMessages.getMessagesBySender(userName));
                receivedMessages.addAll(allMessages.getMessagesByDest(userName));

                // Update the listView for any changes to display all messages
                Collections.sort(sendMessages, new Message_Comperator());
                Collections.sort(receivedMessages, new Message_Comperator());
                sendMessageMode = false;
                listView.setAdapter(receivedMessageAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(null, "loadPost:onCancelled", databaseError.toException());
            }
        });

        // create views and set listener on the buttons of this activity
        send = (Button) findViewById(R.id.send);
        received = (Button) findViewById(R.id.received);
        newMsg = (Button) findViewById(R.id.new_msg);
        send.setOnClickListener(this);
        received.setOnClickListener(this);
        newMsg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == send) {
            // change the buttons color and change ListView adapter
            send.setBackgroundResource(R.drawable.blue_button2);
            received.setBackgroundResource(R.drawable.blue_button);
            sendMessageMode = true;
            listView.setAdapter(sendMessageAdapter);
        }

        else if (v == received) {
            // change the buttons color and change ListView adapter
            received.setBackgroundResource(R.drawable.blue_button2);
            send.setBackgroundResource(R.drawable.blue_button);
            sendMessageMode = false;
            listView.setAdapter(receivedMessageAdapter);
        }

        else if (v == newMsg) {
            // open custom dialog
            createDialog();
        }

        else if(v == sendBtn) {
            // get message information
            String dest = destination.getText().toString();
            String subj = subject.getText().toString();
            String cont = content.getText().toString();

            if(dest.equals("") || subj.equals("") || cont.equals(""))
                Toast.makeText(getApplicationContext(), "Error: all input fields must be filled", Toast.LENGTH_LONG).show();
            else {
                // add message to DB
                Message m = new Message(subj, userName, dest, cont, new MyDate());
                allMessages.addMessage(m);
                dbRef.setValue(allMessages, listener);
                newMsgDialog.dismiss();
            }
        }

        else if(v == cancelBtn) {
            newMsgDialog.dismiss();
        }
    }

    public static boolean displayMessagesSend(){
        return sendMessageMode;
    }

    public void createDialog(){
        // set dialog Properties
        newMsgDialog = new Dialog(this);
        newMsgDialog.setContentView(R.layout.new_private_message_dialog);
        newMsgDialog.setCancelable(true);
        newMsgDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // initialize views
        destination = (EditText)newMsgDialog.findViewById(R.id.destination);
        subject = (EditText)newMsgDialog.findViewById(R.id.subject);
        content = (EditText)newMsgDialog.findViewById(R.id.content);
        sendBtn = (Button)newMsgDialog.findViewById(R.id.send);
        cancelBtn = (Button)newMsgDialog.findViewById(R.id.cancel);
        sendBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        // open dialog
        newMsgDialog.show();
    }

    // Listener that inform the user if the information is stored properly in thw DB or not
    DatabaseReference.CompletionListener listener = new DatabaseReference.CompletionListener() {
        @Override
        public void onComplete(DatabaseError dbError, DatabaseReference dbReference) {
            if (dbError != null)
                Toast.makeText(getApplicationContext(),dbError.getMessage(), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "Message successfully sent", Toast.LENGTH_LONG).show();
        }
    };
}