package com.example.paotonet.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.paotonet.Objects.Child;
import com.example.paotonet.Objects.Parent;
import com.example.paotonet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChildrenInfoAdapter extends ArrayAdapter<Child> {
    private Context context;
    private List<Child> children;
    private ArrayList<Parent> allParents;

    public ChildrenInfoAdapter(Context context, int resource, int textViewResourceId, List<Child> children) {
        super(context, resource, textViewResourceId, children);
        this.context = context;
        this.children = children;
        this.allParents = new ArrayList<Parent>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // create listView
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View listView = layoutInflater.inflate(R.layout.custom_child_info, null, false);

        // create all the child views
        ImageView img = (ImageView) listView.findViewById(R.id.child_image);
        TextView name = (TextView) listView.findViewById(R.id.child_name);
        TextView id = (TextView) listView.findViewById(R.id.id);
        TextView birthdate = (TextView) listView.findViewById(R.id.birthdate);
        TextView parentName = (TextView) listView.findViewById(R.id.parent);
        TextView phone = (TextView) listView.findViewById(R.id.phone);
        TextView moreInfo = (TextView) listView.findViewById(R.id.more_info);

        // initial views to child data
        final Child child = children.get(position);
        name.setText(child.getName());
        id.setText("Id: "+String.valueOf(child.getId()));
        birthdate.setText("Birthdate: "+child.getBirthDate().toDateString());
        moreInfo.setText("More Information: "+child.getMoreInfo());

        // initial views to parent data
        Parent p = getParent(child.getId());
        parentName.setText("Parent: "+p.getName());
        phone.setText("Phone: "+p.getPhone());

        // find ID of child image and set the image to that ID
        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(child.getImg(), "drawable", context.getPackageName());
        img.setImageResource(resourceId);

        // if child clicked it open/close its content
        ConstraintLayout childInfo = (ConstraintLayout)listView.findViewById(R.id.child_info);
        childInfo.setVisibility(View.GONE);
        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (childInfo.getVisibility() == View.GONE)
                    childInfo.setVisibility(View.VISIBLE);
                else
                    childInfo.setVisibility(View.GONE);
            }
        });

        return listView;
    }

    public void getAllParents(int kindergartenId) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users/parents");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    Parent p = data.getValue(Parent.class);
                    if(p.getKindergartenId() == kindergartenId) {
                        allParents.add(p);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(null, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    public Parent getParent(int childId) {
        for(Parent p : allParents) {
            if(p.getChildId() == childId) {
                return p;
            }
        }
        return new Parent();
    }
}