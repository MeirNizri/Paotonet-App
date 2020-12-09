package com.example.paotonet.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.paotonet.Objects.Child;
import com.example.paotonet.Objects.Message;
import com.example.paotonet.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {
    private Context context;
    private List<Message> messages;
    ArrayList<Child> children = new ArrayList<>();

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
        TextView senderName = (TextView) listView.findViewById(R.id.sender_name);
        TextView content = (TextView) listView.findViewById(R.id.message_content);
        TextView timeSend = (TextView) listView.findViewById(R.id.time_send);

        // initial views to message data
        final Message message = messages.get(position);
        senderName.setText("From: " + message.getSender());
        content.setText(message.getContent());
        timeSend.setText(message.toTimeString());

        return listView;
    }
}
