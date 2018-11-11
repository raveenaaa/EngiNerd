package com.example.raveena.myapplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    //defining view objects
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignup;
    private EditText name;
    private Spinner cname, spinner;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private String branch, college;
    private TextView textViewSignin;
    private ProgressDialog progressDialog;
    private Student stud;

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;  //reference  to root of db

    // Date objects
    private static EditText bday;
    private TextView selectBday;
    private static int year;
    private static int month;
    private static int day;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);

        //initializing firebase auth, database objects
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        ref = db.getReference().child("students");

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        name = (EditText)findViewById(R.id.name);   //student name
        radioGroup = (RadioGroup)findViewById(R.id.radio);

        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);
        cname = (Spinner)findViewById(R.id.cname);  //college name

        buttonSignup = (Button) findViewById(R.id.button);
        textViewSignin = (TextView)findViewById(R.id.textViewSignin);

        // initializing date components
        bday = (EditText)findViewById(R.id.bday);
        selectBday = (TextView)findViewById(R.id.selectBday);
        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        buttonSignup.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
        bday.setOnClickListener(this);
        selectBday.setOnClickListener(this);


        View.OnTouchListener otl = new View.OnTouchListener() {
            public boolean onTouch (View v, MotionEvent event) {
                return true; // the listener has consumed the event
            }
        };

        //Disable keyboard
        bday.setOnTouchListener(otl);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        cname.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<>();
        categories.add("Aerospace");
        categories.add("Chemical");
        categories.add("Computer");
        categories.add("Civil");
        categories.add("Electrical");
        categories.add("Electronics");
        categories.add("Information Technology");
        categories.add("Mechanical");

        List<String> colleges = new ArrayList<>();
        colleges.add("Atharva");
        colleges.add("DJ Sanghvi");
        colleges.add("Fr Agnels");
        colleges.add("KJ Somaiya");
        colleges.add("SPCE");
        colleges.add("SPIT");
        colleges.add("Thadomal Shahani");
        colleges.add("VJTI");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        ArrayAdapter<String> collAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, colleges);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        collAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        cname.setAdapter(collAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        switch(parent.getId()) {
            case R.id.spinner:
            branch = parent.getItemAtPosition(position).toString();
                break;
            case R.id.cname:
             college = parent.getItemAtPosition(position).toString();
        }
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void registerUser() {

        //getting email and password from edit texts
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String studentName  = name.getText().toString().trim();
        final String collegeName = college;
        final String collegeBranch = branch;
        final String gender = radioButton.getText().toString().trim();
        final int y = year;
        final int d = day;
        final int m = month;


        //checking fields are empty
        if(TextUtils.isEmpty(studentName)){ //name is empty
            Toast.makeText(this, "Please enter name",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(gender)){ //gender is empty
            Toast.makeText(this, "Please select gender",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(email)){ //Email is empty
            Toast.makeText(this, "Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){ //password is empty
            Toast.makeText(this, "Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(collegeName)){ //college name is empty
            Toast.makeText(this, "Please enter college",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(collegeBranch)){ //branch is empty
            Toast.makeText(this, "Please enter branch",Toast.LENGTH_LONG).show();
            return;
        }

        //Check if user entered valid email

        if(!isEmailValid(email)){
            Toast.makeText(this, "Please enter valid email",Toast.LENGTH_LONG).show();
            return;
        }

        if(password.length()<6){
            Toast.makeText(this, "Password should be more than 6 characters",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        if (isOnline()) {

            progressDialog.setMessage("Registering Please Wait...");
            progressDialog.show();

            //Authenticating a new user
            try {

                stud = new Student(studentName, collegeName, email, collegeBranch, gender, d, m, y);
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //checking if success
                                if (task.isSuccessful()) {

                                    onAuthSuccess(task.getResult().getUser());

                                } else {
                                    //display some message here
                                    FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                    assert e != null;
                                    Log.d(TAG, " " + e.getMessage());
                                    Toast.makeText(RegistrationActivity.this, "Registration Error", Toast.LENGTH_LONG).show();
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));

                                }
                                progressDialog.dismiss();
                            }
                        });

            } catch (Exception e) {
                FirebaseCrash.log("User Authentication:Registration");
                FirebaseCrash.report(e);
                Toast.makeText(getApplicationContext(), "Registration Error", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "You need to be connected to the internet to register!", Toast.LENGTH_SHORT).show();
            return;
        }
    }

     private void onAuthSuccess(FirebaseUser user){
         Toast.makeText(RegistrationActivity.this, "onAuthSuccess", Toast.LENGTH_LONG).show();
         ref.child(user.getUid()).setValue(stud).addOnCompleteListener(new OnCompleteListener<Void>() {
             @Override
             public void onComplete(@NonNull Task<Void> task) {
                 if (task.isSuccessful()) {
                     Toast.makeText(getApplicationContext(), "User Registered", Toast.LENGTH_SHORT).show();
                     finish();
                     startActivity(new Intent(getApplicationContext(), MainActivity.class));
                 } else {
                     Toast.makeText(getApplicationContext(), "Registration Error", Toast.LENGTH_SHORT).show();
                     finish();
                     startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
                 }
             }
         });
     }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed(){
        finish();
        startActivity(new Intent(this,MainActivity.class));
    }


    @Override
    public void onClick(View v) {
        if(v == buttonSignup && isOnline()) {
            radioButton = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
            registerUser();

        }
        if(v == textViewSignin && isOnline()){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        if(v==selectBday){
            showDatePickerDialog(v);
        }
    }

    public void showDatePickerDialog(View v) {
        try {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        }
        catch(Exception e){
            Log.w(TAG,""+e.getMessage());
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            updateEditText(year, month, day);

        }
    }

    public static void updateEditText(int y, int m, int d){

        bday.setText(d+"/"+(m+1)+"/"+y);
        year = y;
        month = m+1;
        day = d;

    }
}
