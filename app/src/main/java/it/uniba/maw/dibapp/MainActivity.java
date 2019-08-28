package it.uniba.maw.dibapp;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import it.uniba.maw.dibapp.fragment.CalendarFragment;
import it.uniba.maw.dibapp.fragment.SettingsFragment;
import it.uniba.maw.dibapp.model.Lezione;
import it.uniba.maw.dibapp.util.Util;

import static it.uniba.maw.dibapp.util.Util.DEBUG_TAG;
import static it.uniba.maw.dibapp.util.Util.lezioniList;

public class MainActivity extends AppCompatActivity {

    private ActionBar toolbar;
    CalendarFragment calendarFragment;
    SettingsFragment settingsFragment;
    Fragment fragmentVuoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = getSupportActionBar();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // load the store fragment by default
        toolbar.setTitle("DibApp");

        calendarFragment = new CalendarFragment();
        settingsFragment = new SettingsFragment();
        fragmentVuoto = new Fragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_container,calendarFragment,"calendar")
                .add(R.id.frame_container,settingsFragment,"settings")
                .hide(settingsFragment)
                .add(R.id.frame_container,fragmentVuoto,"vuoto")
                .hide(fragmentVuoto)
                .commit();
        //loadFragment(calendarFragment);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.calendar:
                    toolbar.setTitle("Calendar");
                    loadFragment(calendarFragment);
                    return true;
                case R.id.navigation_gifts:
                    toolbar.setTitle("Lezioni di oggi");
                    loadFragment(fragmentVuoto);
                    return true;
                case R.id.settings:
                    toolbar.setTitle("Settings");
                    loadFragment(settingsFragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragmentVuoto);
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



}
