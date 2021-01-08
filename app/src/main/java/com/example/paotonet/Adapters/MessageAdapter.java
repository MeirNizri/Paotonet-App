package com.example.paotonet.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.paotonet.Activities.PrivateMessages;
import com.example.paotonet.Objects.Message;
import com.example.paotonet.R;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {
    private Context context;
    private List<Message> messages;

    public MessageAdapter(Context context, int resource, int textViewResourceId, List<Message> messages) {
        super(context, resource, textViewResourceId, messages);
        this.context = context;
        this.messages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // create listView
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View listView = layoutInflater.inflate(R.layout.custom_message, null, true);

        // create views
        TextView head = (TextView) listView.findViewById(R.id.head);
        TextView title = (TextView) listView.findViewById(R.id.title);
        TextView timeSend = (TextView) listView.findViewById(R.id.time_send);
        TextView content = (TextView) listView.findViewById(R.id.content);

        // initial views to message data.
        final Message message = messages.get(position);
        if(PrivateMessages.displayMessagesSend())
            head.setText("To: " + message.getDestination());
        else
            head.setText("From: " + message.getSender());
        title.setText("Subject: " + message.getTitle());
        timeSend.setText(message.toTimeAndDateString());
        content.setText(message.getContent());
        content.setVisibility(View.GONE);

        // if message clicked open/close its hidden content
        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (content.getVisibility() == View.GONE)
                    content.setVisibility(View.VISIBLE);
                else
                    content.setVisibility(View.GONE);
            }
        });

        return listView;
    }
}
