package com.example.raveena.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.facebook.FacebookSdk.getApplicationContext;


//Our class extending fragment
public class Tab2 extends Fragment {
    private DatabaseReference mDatabase;

    private FirebaseRecyclerAdapter<Rating, RatingViewHolder> mAdapter;
    private LinearLayoutManager mManager;
    private RecyclerView mRatingList;
    private String name;

    // Overridden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
               super.onCreateView(inflater, container, savedInstanceState);
          View rootView =  inflater.inflate(R.layout.tab2, container, false);

        mRatingList = (RecyclerView) rootView.findViewById(R.id.ratingList);
        mRatingList.setHasFixedSize(true);

        return rootView;
    }

    // Overridden method onActivityCreated
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        try {
            name = ((SearchResult)getActivity()).getCollege();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("rating/"+name);

            // Set up Layout Manager, reverse layout
            mManager = new LinearLayoutManager(getActivity());
            mManager.setReverseLayout(true);
            mManager.setStackFromEnd(true);
            mRatingList.setLayoutManager(mManager);

            mAdapter = new FirebaseRecyclerAdapter<Rating, RatingViewHolder>(
                    Rating.class,
                    R.layout.item_rating,
                    RatingViewHolder.class,
                    mDatabase) {
                @Override
                protected void populateViewHolder(RatingViewHolder viewHolder, Rating model, int position) {

                    viewHolder.setElements(model);
                }
            };
            mRatingList.setAdapter(mAdapter);
        }
        catch(Exception e){
            Log.v("Tab2", ""+e.getMessage());
            Toast.makeText(getApplicationContext(), "onActivityCreated",Toast.LENGTH_LONG).show();
        }

    }
}