package it.uniba.maw.dibapp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import it.uniba.maw.dibapp.fragment.CalendarFragment;
import it.uniba.maw.dibapp.fragment.LezioniDelGiornoFragment;
import it.uniba.maw.dibapp.fragment.SettingsFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar drawer_toolbar;
    CalendarFragment calendarFragment;
    SettingsFragment settingsFragment;
    LezioniDelGiornoFragment lezioniDelGiornoFragment;
    ProgressBar progressBar;
    BottomNavigationView navigation;

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
        //progressShow();

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // load the store fragment by default
        drawer_toolbar.setTitle("Lezioni del giorno");

        progressShow();
        initializeFragment();
    }

    @Override
    protected void onResume() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("NEW_LESSON");
        registerReceiver(newLessonReceiver,intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(newLessonReceiver);
        super.onPause();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_account: //TODO//
                break;
            case R.id.nav_calendar:
                drawer_toolbar.setTitle("Calendar");
                loadFragment(calendarFragment);
                break;
            case R.id.nav_lesson:
                drawer_toolbar.setTitle("Lezioni di oggi");
                loadFragment(lezioniDelGiornoFragment);
                break;
            case R.id.nav_settings:
                drawer_toolbar.setTitle("Settings");
                loadFragment(settingsFragment);
                break;
            case R.id.nav_faq: //TODO//
                break;
            case R.id.nav_info: //TODO//
                break;
            case R.id.nav_logoout:
                showPopup();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
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

            // check whether the item showing badge
            if (navigation.getBadge(item.getItemId()) != null && navigation.getBadge(item.getItemId()).isVisible())
                navigation.removeBadge(item.getItemId());  //  remove badge notification

            switch (item.getItemId()) {
                case R.id.calendar:
                    drawer_toolbar.setTitle(R.string.calendar);
                    loadFragment(calendarFragment);
                    return true;
                case R.id.todayLessons:
                    drawer_toolbar.setTitle(R.string.lessons);
                    loadFragment(lezioniDelGiornoFragment);
                    return true;
                case R.id.settings:
                    drawer_toolbar.setTitle(R.string.settings);
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

    BroadcastReceiver newLessonReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("NEW_LESSON")) {
                BadgeDrawable badge = navigation.showBadge(R.id.todayLessons);
                if(!badge.hasNumber()){
                    badge.setNumber(badge.getNumber()+1);
                }else{
                    badge.setNumber(1);
                }
            }
        }
    };
}
