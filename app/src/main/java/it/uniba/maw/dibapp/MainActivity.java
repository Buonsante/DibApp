package it.uniba.maw.dibapp;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ToolbarWidgetWrapper;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import it.uniba.maw.dibapp.fragment.CalendarFragment;
import it.uniba.maw.dibapp.fragment.LezioniDelGiornoFragment;
import it.uniba.maw.dibapp.fragment.SettingsFragment;
import it.uniba.maw.dibapp.model.Lezione;
import it.uniba.maw.dibapp.util.Util;

import static it.uniba.maw.dibapp.util.Util.DEBUG_TAG;
import static it.uniba.maw.dibapp.util.Util.SHARED_PREFERENCE_NAME;
import static it.uniba.maw.dibapp.util.Util.lezioniList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar drawer_toolbar;
    CalendarFragment calendarFragment;
    SettingsFragment settingsFragment;
    LezioniDelGiornoFragment lezioniDelGiornoFragment;
    ProgressBar progressBar;
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawer_toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, drawer_toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        progressBar = findViewById(R.id.mainProgressBar);
        progressShow();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // load the store fragment by default
        drawer_toolbar.setTitle("DibApp");

        getLezioni();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_logoout: showPopup();

                break;
        }

        return true;
    }

    // first step helper function
    private void showPopup() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setMessage("Are you sure?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener()                 {

                    public void onClick(DialogInterface dialog, int which) {

                        logout(); // Last step. Logout function

                    }
                }).setNegativeButton("Cancel", null);

        AlertDialog alert1 = alert.create();
        alert1.show();
    }

    private void logout() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.calendar:
                    drawer_toolbar.setTitle("Calendar");
                    loadFragment(calendarFragment);
                    return true;
                case R.id.navigation_gifts:
                    drawer_toolbar.setTitle("Lezioni di oggi");
                    loadFragment(lezioniDelGiornoFragment);
                    return true;
                case R.id.settings:
                    drawer_toolbar.setTitle("Settings");
                    loadFragment(settingsFragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(lezioniDelGiornoFragment);
        transaction.hide(settingsFragment);
        transaction.hide(calendarFragment);
        transaction.show(fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();

    }


//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            Fragment fragment;
//            switch (item.getItemId()) {
//                case R.id.calendar:
//                    toolbar.setTitle("Calendar");
//                    fragment = new CalendarFragment();
//                    loadFragment(fragment);
//                    return true;
//                case R.id.navigation_gifts:
//                    toolbar.setTitle("My Gifts");
//                    fragment = new Fragment();
//                    loadFragment(fragment);
//                    return true;
//                case R.id.settings:
//                    toolbar.setTitle("Settings");
//                    fragment = new SettingsFragment();
//                    loadFragment(fragment);
//                    return true;
//            }
//            return false;
//        }
//    };

//    private void loadFragment(Fragment fragment) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.frame_container, fragment);
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        //transaction.addToBackStack(null);
//        transaction.commit();
//    }

    private void getLezioni(){
        lezioniList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query query;
        if(getSharedPreferences(SHARED_PREFERENCE_NAME,MODE_PRIVATE).getString("tipo","").equals("D"))
            query = db.collectionGroup("lezioni").whereEqualTo("professore","Denaro Roberto");
        else
            query = db.collectionGroup("lezioni");

        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.w(DEBUG_TAG,"Retrieve Lezioni");
                        for(DocumentSnapshot document : queryDocumentSnapshots.getDocuments()){
                            Lezione l = document.toObject(Lezione.class);
                            l.setLinkLezione(document.getReference().getPath());
                            //Log.w(DEBUG_TAG,"Lezione: "+l.toString());
                            Util.lezioniList.add(l);
                        }
                        Log.w(DEBUG_TAG,"LESSONS RETRIEVED");

                        initializeFragment();
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(DEBUG_TAG+"err",e.getMessage());
                    }
                });
    }

    void initializeFragment() {
        calendarFragment = new CalendarFragment();
        settingsFragment = new SettingsFragment();
        lezioniDelGiornoFragment = new LezioniDelGiornoFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_container,calendarFragment,"calendar")
                .add(R.id.frame_container,settingsFragment,"settings")
                .hide(settingsFragment)
                .add(R.id.frame_container, lezioniDelGiornoFragment,"lezioniDelGiorno")
                .hide(calendarFragment)
                .commitNow();
        //loadFragment(calendarFragment);
        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void progressShow() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

}
