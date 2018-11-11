package com.example.raveena.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RatingActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TAG";
    private RatingBar infra;
    private RatingBar placement;
    private RatingBar crowd;
    private RatingBar faculty;
    private EditText comments;
    private Button submit, anon;
    private TextView collname;
    String username,college;

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;  //reference  to root of db

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        college = bundle.getString("college");

        //initializing firebase auth, database objects
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        ref = db.getReference().child("rating/"+college);

        infra = (RatingBar) findViewById(R.id.rateInfra);
        placement = (RatingBar) findViewById(R.id.ratePlacement);
        crowd = (RatingBar) findViewById(R.id.rateCrowd);
        faculty = (RatingBar) findViewById(R.id.rateFaculty);
        submit = (Button) findViewById(R.id.submit);
        anon = (Button) findViewById(R.id.anon);
        comments = (EditText) findViewById(R.id.comments);
        collname = (TextView) findViewById(R.id.college);
        collname.setText(college);

        submit.setOnClickListener(this);
        anon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(isOnline()) {
            if (v == submit) {
                float inf = infra.getRating();
                float plc = placement.getRating();
                float crd = crowd.getRating();
                float fac = faculty.getRating();
                String com = comments.getText().toString().trim();
                String coll = collname.getText().toString().trim();
                try {
                    String uid = firebaseAuth.getCurrentUser().getUid();
                    Rating r = new Rating(coll, username, com, inf, plc, crd, fac);
                    ref.child(uid).setValue(r);
                } catch (Exception e) {
                    Log.v(TAG, "Email error:" + e.getMessage());
                }
            }
            else if(v == anon){
                float inf = infra.getRating();
                float plc = placement.getRating();
                float crd = crowd.getRating();
                float fac = faculty.getRating();
                String com = comments.getText().toString().trim();
                String coll = collname.getText().toString().trim();
                try {
                    String user = "Anonymous";
                    String uid = firebaseAuth.getCurrentUser().getUid();
                    Rating r = new Rating(coll, user, com, inf, plc, crd, fac);
                    ref.child(coll + "/" + uid).setValue(r);
                } catch (Exception e) {
                    Log.v(TAG, "Email error:" + e.getMessage());
                  }
               }
               finish();
            startActivity(new Intent(this, MainActivity.class));
            }
        else {
            Toast.makeText(this, "You need to be connected to the internet!!", Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    public void onBackPressed(){
        finish();
        startActivity(new Intent(this,MainActivity.class));
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
