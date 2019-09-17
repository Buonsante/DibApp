package it.uniba.maw.dibapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import it.uniba.maw.dibapp.fragment.BottomSheetFragment;
import it.uniba.maw.dibapp.fragment.CommentiAdapter;
import it.uniba.maw.dibapp.fragment.CommentiFragment;
import it.uniba.maw.dibapp.fragment.DettagliFragment;
import it.uniba.maw.dibapp.util.Util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.FloatMath;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.google.android.material.tabs.TabLayout;

import static it.uniba.maw.dibapp.util.Util.DEBUG_TAG;

public class LessonActivity extends AppCompatActivity {

    private static final String TAG = "LessonActivity";

    private SectionsPageAdapter mSectionsPageAdapter;

    private Toolbar toolbar;

    private Button buttonDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        Log.d(DEBUG_TAG, "onCreate: Starting");

        toolbar = findViewById(R.id.lesson_toolbar);
        toolbar.setTitle(R.string.lesson_detail);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




        buttonDialog = findViewById(R.id.btn_bottom_sheet);

            buttonDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showBottomSheetDialog();
                }
            });

            //se l'utente è un docente nasconde il pulsante di aggiunta commenti
            if (getSharedPreferences(Util.SHARED_PREFERENCE_NAME, MODE_PRIVATE).getString("tipo", "").equals("D")) {
                buttonDialog.setVisibility(View.INVISIBLE);
            }else{

            }


            mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

            //Set up the ViewPager with the sections adapter
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);

            setupViewPager(mViewPager);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);




    }



    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        DettagliFragment dettagliFragment = new DettagliFragment();
        CommentiFragment commentiFragment = new CommentiFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("lezione", getIntent().getSerializableExtra("lezione"));
        dettagliFragment.setArguments(bundle);
        commentiFragment.setArguments(bundle);
        adapter.addFragment(dettagliFragment, "DETTAGLI");
        adapter.addFragment(commentiFragment, "COMMENTI");
        viewPager.setAdapter(adapter);
    }

    private void showBottomSheetDialog() {
//        // using BottomSheetDialog
//        View dialogView = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet, null);
//        BottomSheetDialog dialog = new BottomSheetDialog(this);
//        dialog.setContentView(dialogView);
//        dialog.show();

        // using BottomSheetDialogFragment
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        
        //passa la lezione corrente al bottom sheet fragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("lezione", getIntent().getSerializableExtra("lezione"));
        bottomSheetFragment.setArguments(bundle);

        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }





}
