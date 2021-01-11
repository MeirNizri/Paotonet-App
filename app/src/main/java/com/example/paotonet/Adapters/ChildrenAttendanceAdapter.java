package com.example.paotonet.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.paotonet.Objects.Child;
import com.example.paotonet.Objects.MyDate;
import com.example.paotonet.R;

import java.util.ArrayList;
import java.util.List;

public class ChildrenAttendanceAdapter extends ArrayAdapter<Child> {
    TextView health_declaration_msg;
    boolean result;
    private Context context;
    private List<Child> children;
    private ArrayList<Integer> presentChildrenID = new ArrayList<Integer>();

    public ChildrenAttendanceAdapter(Context context, int resource, int textViewResourceId, List<Child> children) {
        super(context, resource, textViewResourceId, children);
        this.context = context;
        this.children = children;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // create listView
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View listView = layoutInflater.inflate(R.layout.custom_child_attendance, null, false);

        // create views
        TextView child_name = (TextView) listView.findViewById(R.id.child_name);
        health_declaration_msg = (TextView) listView.findViewById(R.id.health_declaration_msg);
        RadioGroup rg_attendance = (RadioGroup) listView.findViewById(R.id.attendance_check);
        ImageView child_img = (ImageView) listView.findViewById(R.id.child_image);

        // initial views to child data
        final Child child = children.get(position);
        child_name.setText(child.getName());

        // inform if the child parents signed health declaration
        MyDate current = new MyDate();
        if (child.getLastSignedDeclaration().equals(current.toDateString())) {
            health_declaration_msg.setText("Signed health declaration");
            health_declaration_msg.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            health_declaration_msg.setText("didn't signed health declaration");
            health_declaration_msg.setTextColor(context.getResources().getColor(R.color.red));
        }

        // find ID of child image and set the image to that ID
        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(child.getImg(), "drawable", context.getPackageName());
        child_img.setImageResource(resourceId);

        // set listeners on the radio group
        rg_attendance.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find the radio button checked name: "present"/"absent"
                RadioButton rbChecked = ((RadioButton) listView.findViewById(rg_attendance.getCheckedRadioButtonId()));
                final String rbName = rbChecked.getResources().getResourceEntryName(rbChecked.getId());
                //if child checked as present and he's not already in the array - add him
                if (rbName.contentEquals("present") && !presentChildrenID.contains(child.getId()))
                    presentChildrenID.add(child.getId());
                    // if child checked as absent and he's in the array - remove him
                else if (rbName.contentEquals("absent") && presentChildrenID.contains(child.getId()))
                    presentChildrenID.remove(Integer.valueOf(child.getId()));
            }
        });
        return listView;
    }

    // returns the ID of all the current checked children
    public ArrayList<Integer> getPresentChildrenID() {
        return presentChildrenID;
    }
}