package it.uniba.maw.dibapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import it.uniba.maw.dibapp.fragment.CalendarFragment;
import it.uniba.maw.dibapp.fragment.SettingsFragment;
import it.uniba.maw.dibapp.model.Lezione;

public class ProfessorActivity extends AppCompatActivity {

    private ActionBar toolbar;
    public List<Lezione> lezioni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = getSupportActionBar();

        Button btnHome=(Button)findViewById(R.id.Button1);
        btnHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                // definisco l'intenzione
                Intent openLesson = new Intent(ProfessorActivity.this,LessonActivity.class);
                // passo all'attivazione dell'activity Lesson.java
                startActivity(openLesson);
            }
        });
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // load the store fragment by default
        toolbar.setTitle("Shop");
        loadFragment(new SettingsFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.calendar:
                    toolbar.setTitle(R.string.calendar);
                    fragment = new CalendarFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_gifts:
                    toolbar.setTitle(R.string.lessons);
                    fragment = new Fragment();
                    loadFragment(fragment);
                    return true;
                case R.id.settings:
                    toolbar.setTitle(R.string.settings);
                    fragment = new SettingsFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        //transaction.addToBackStack("fragmentStack");
        transaction.commit();
    }


}
