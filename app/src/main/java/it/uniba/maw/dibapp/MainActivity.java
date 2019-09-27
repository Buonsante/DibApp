package it.uniba.maw.dibapp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

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
    private TextView textViewFullNameNav;
    private TextView textViewEmailNav;
    NavigationView navigationView;

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

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        progressBar = findViewById(R.id.mainProgressBar);
        progressShow();

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        // load the store fragment by default
        drawer_toolbar.setTitle(R.string.today_lessons);

        View headerView = navigationView.getHeaderView(0);

        textViewFullNameNav = headerView.findViewById(R.id.textViewFullNameNav);
        textViewEmailNav = headerView.findViewById(R.id.textViewEmailNav);

        SharedPreferences prefs = getSharedPreferences("profile data", MODE_PRIVATE);

        textViewFullNameNav.setText(prefs.getString("nome","") + " " + prefs.getString("cognome", " "));
        textViewEmailNav.setText(prefs.getString("mail", ""));

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
            case R.id.nav_account:
                Intent userActivityIntent = new Intent(this, UserActivity.class);
                startActivity(userActivityIntent);
                break;
            case R.id.nav_calendar:
                drawer_toolbar.setTitle(R.string.calendar);
                loadFragment(calendarFragment);
                break;
            case R.id.nav_lesson:
                drawer_toolbar.setTitle(R.string.today_lessons);
                loadFragment(lezioniDelGiornoFragment);
                break;
            case R.id.nav_settings:
                drawer_toolbar.setTitle(R.string.settings);
                loadFragment(settingsFragment);
                break;
            case R.id.nav_faq: //Not handled
                break;
            case R.id.nav_info: //Not handled
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
        alert.setMessage(R.string.logout_alert_string)
                .setPositiveButton(R.string.string_logout, new DialogInterface.OnClickListener()                 {
                    public void onClick(DialogInterface dialog, int which) {
                        logout(); // Last step. Logout function
                    }
                }).setNegativeButton(R.string.cancel, null);

        AlertDialog alert1 = alert.create();
        alert1.show();
    }

    private void logout() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //modifica il token dell'user nel database
        db.collection("utenti").whereEqualTo("mail",user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            queryDocumentSnapshots.getDocuments().get(0).getReference().update("token","");
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            }
        });

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
                    drawer_toolbar.setTitle(R.string.today_lessons);
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

        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        navigation.setSelectedItemId(R.id.todayLessons);
    }

    public void progressShow() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    BroadcastReceiver newLessonReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("NEW_LESSON")) {
                BadgeDrawable badge = navigation.getOrCreateBadge(R.id.todayLessons);
                badge.setVisible(true);
                if(!badge.hasNumber()){
                    badge.setNumber(badge.getNumber()+1);
                }else{
                    badge.setNumber(1);
                }
            }
        }
    };
}
