package it.uniba.maw.dibapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import it.uniba.maw.dibapp.fragment.BottomSheetFragment;
import it.uniba.maw.dibapp.fragment.CommentiFragment;
import it.uniba.maw.dibapp.fragment.DettagliFragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

public class LessonActivity extends AppCompatActivity {

    private static final String TAG = "LessonActivity";

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    private Button buttonDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        Log.d(TAG, "onCreate: Starting");

        buttonDialog = findViewById(R.id.btn_bottom_sheet);

        buttonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();
            }
        });


        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        //Set up the ViewPager with the sections adapter
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        DettagliFragment dettagliFragment = new DettagliFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("lezione", getIntent().getSerializableExtra("lezione"));
        dettagliFragment.setArguments(bundle);
        adapter.addFragment(dettagliFragment, "DETTAGLI");
        adapter.addFragment(new CommentiFragment(), "COMMENTI");
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
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }
}
