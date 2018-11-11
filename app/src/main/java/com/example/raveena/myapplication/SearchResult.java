package com.example.raveena.myapplication;

import android.app.TabActivity;
import android.database.Cursor;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchResult extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private static final String TAG = "Display exception";
    private TabLayout tabLayout;       //This is our tablayout
    private Toolbar toolbar;
    private ViewPager viewPager;       //This is our viewPager
    public String loc = "", name = "";
    private List<College> mCollege = new ArrayList<>();
    private CollegeAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        // Adding tabs to tablayout
        tabLayout.addTab(tabLayout.newTab().setText("Gallery"));
        tabLayout.addTab(tabLayout.newTab().setText("Reviews"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

        //Creating our pager adapter
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Adding onTabSelectedListener to swipe views
        tabLayout.addOnTabSelectedListener(this);

        int id = getIntent().getExtras().getInt("id");
        SqliteHandler myDatabase = new SqliteHandler(this);

        Cursor cursor = myDatabase.getAllCollegesDescription(id + 1);
        if (cursor.getCount() < 1) {
            loc += "No Data Available";
            name += "No Data Available";

        } else {
            cursor.moveToFirst();
            do {
                loc = cursor.getString(cursor.getColumnIndex(SqliteHandler.COLLEGE_LOC));
                name = cursor.getString(cursor.getColumnIndex(SqliteHandler.COLLEGE_NAME));
            }
            while (cursor.moveToNext());
        }

        ListView collegeDetails = (ListView) findViewById(R.id.collegeDetails);
        arrayAdapter = new CollegeAdapter(this, R.layout.item_college, mCollege);
        collegeDetails.setAdapter(arrayAdapter);

        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("college").child(name);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mCollege.clear();
                    College coll = dataSnapshot.getValue(College.class);
                    mCollege.add(coll);
                    arrayAdapter.notifyDataSetChanged();
               }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        catch(Exception e){
            FirebaseCrash.logcat(Log.INFO, TAG, "App state:"+e.getMessage());
            FirebaseCrash.report(e);
        }

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public String getCollege(){
        return name;
    }
}
