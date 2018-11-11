package com.example.raveena.myapplication;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class StudentAdapter extends ArrayAdapter {

    public StudentAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            convertView = vi.inflate(R.layout.item_user, null);
        }

        TextView username = (TextView) convertView.findViewById(R.id.userName);
        TextView email = (TextView) convertView.findViewById(R.id.userEmail);

        Student stud = (Student) getItem(position);

       username.setText(stud.getSname());
       email.setText(stud.getEmail());

        return convertView;
    }
}
