package com.example.raveena.myapplication;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CollegeAdapter extends ArrayAdapter {

    public CollegeAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            convertView = vi.inflate(R.layout.item_college, null);
        }

        TextView cname = (TextView) convertView.findViewById(R.id.cname);
        TextView address = (TextView) convertView.findViewById(R.id.address);

        College coll = (College) getItem(position);

        cname.setText(coll.getCname());
        address.setText(coll.getAddress());

        return convertView;
    }
}
