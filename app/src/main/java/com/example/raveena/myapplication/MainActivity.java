package com.example.raveena.myapplication;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, SearchView.OnSuggestionListener{
    private Button logIn, sign;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private SearchView searchBox;
    private SqliteHandler myDatabase;
    private ListView userDetails;
    private List<Student> mUser = new ArrayList<>();

    //firebase auth object
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference ref;
    private FirebaseUser user;
    private String collname, nom;

    private static final String TAG="TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logIn = (Button)findViewById(R.id.login);
        sign = (Button)findViewById(R.id.signUp);
        searchBox = (SearchView) findViewById(R.id.search);
        try {
            myDatabase = new SqliteHandler(this);
        }
        catch(Exception e){
            FirebaseCrash.logcat(Log.INFO, TAG, "App state:"+e.getMessage());
            FirebaseCrash.report(e);
        }

        LinearLayout linear = (LinearLayout) findViewById(R.id.linear);
        linear.getBackground().setAlpha(160);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        logIn.setOnClickListener(this);
        sign.setOnClickListener(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        hideItem();

        toolbar.setNavigationIcon(null);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in

                    toolbar.setNavigationIcon(R.mipmap.ic_menu_white_24dp);
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    try {
                       userDetails = (ListView)findViewById(R.id.userDetails);
                       final StudentAdapter arrayAdapter = new StudentAdapter(getApplicationContext(), R.layout.item_user, mUser);
                       userDetails.setAdapter(arrayAdapter);
                        ref = FirebaseDatabase.getInstance().getReference().child("students").child(user.getUid());
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(DataSnapshot dataSnapshot) {
                               mUser.clear();
                            Student student = dataSnapshot.getValue(Student.class);
                               mUser.add(student);
                               arrayAdapter.notifyDataSetChanged();

                           }

                           @Override
                           public void onCancelled(DatabaseError databaseError) {

                           }
                       });

                    }catch(Exception e){
                        FirebaseCrash.log("Email");
                        FirebaseCrash.report(e);
                    }
                }
                else{
                    toolbar.setNavigationIcon(null);
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }
            }
        };

        try {

            final SearchManager searchManager = (SearchManager)
                    getSystemService(Context.SEARCH_SERVICE);
            searchBox.setSearchableInfo(searchManager.getSearchableInfo(
                    new ComponentName(this, SearchResult.class)));
            searchBox.setOnSuggestionListener(this);
            searchBox.setIconifiedByDefault(false);
        }
        catch (Exception e){
            FirebaseCrash.logcat(Log.INFO, TAG, "App state:"+e.getMessage());
            FirebaseCrash.report(e);
        }
    }

    private class StudentAdapter extends ArrayAdapter {

        StudentAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
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
            collname = stud.getCname();
            nom = stud.getSname();
            return convertView;
        }
    }

    @Override
    public void onStart(){
        super.onStart();

        firebaseAuth.addAuthStateListener(mAuthListener);
        LinearLayout buttons = (LinearLayout)findViewById(R.id.buttons);
        if (firebaseAuth.getCurrentUser() != null) {
             buttons.setVisibility(View.INVISIBLE);
        }
        else{
            buttons.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        firebaseAuth.addAuthStateListener(mAuthListener);
        LinearLayout buttons = (LinearLayout)findViewById(R.id.buttons);
        if (firebaseAuth.getCurrentUser() != null) {
            buttons.setVisibility(View.INVISIBLE);
        }
        else{
            buttons.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        } else if (id == R.id.nav_account) {


        } else if (id == R.id.nav_about) {


        } else if (id == R.id.nav_logOut) {
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        else if(id == R.id.nav_rating){
            Intent intent = new Intent(this, RatingActivity.class);
            intent.putExtra("username", nom);
            intent.putExtra("college", collname);
            finish();
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void hideItem()  //Hide Home option in home page's navbar
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_home).setVisible(false);

    }


    @Override
    public void onClick(View v) {
        if(v==logIn){
            startActivity((new Intent(this, LoginActivity.class)));
        }

        if(v==sign){
            startActivity((new Intent(this, RegistrationActivity.class)));
        }
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return true;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        int id = (int) searchBox.getSuggestionsAdapter().
                getItemId(position);

        Intent intent = new Intent(this, SearchResult.class);

        intent.putExtra("id", id);

        startActivity(intent);

        return true;
    }
}
