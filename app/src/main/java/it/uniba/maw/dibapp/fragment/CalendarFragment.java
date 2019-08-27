package it.uniba.maw.dibapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import it.uniba.maw.collapsiblecalendar.widget.CollapsibleCalendar;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.List;

import it.uniba.maw.dibapp.MainActivity;
import it.uniba.maw.dibapp.R;
import it.uniba.maw.dibapp.model.Lezione;
import it.uniba.maw.dibapp.util.Util;

import static it.uniba.maw.dibapp.util.Util.DEBUG_TAG;
import static it.uniba.maw.dibapp.util.Util.lezioniList;


public class CalendarFragment extends Fragment {

    private CollapsibleCalendar collapsibleCalendar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private SharedPreferences pref;
    private List<Lezione> lezioni;
    private Bundle savedState;

    public CalendarFragment() {
        // Required empty public constructor
    }

    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    }

    void addEvent(Lezione l){
        collapsibleCalendar.addEventTag(l.getGregorianData().get(Calendar.YEAR), l.getGregorianData().get(Calendar.MONTH), l.getGregorianData().get(Calendar.DAY_OF_MONTH));
    }

    private void getLezioni(){
        lezioniList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collectionGroup("lezioni").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.w(DEBUG_TAG,"Retrieve Lezioni");
                        for(DocumentSnapshot document : queryDocumentSnapshots.getDocuments()){
                            Lezione l = document.toObject(Lezione.class);
                            Log.w(DEBUG_TAG,"Lezione: "+l.toString());
                            Util.lezioniList.add(l);
                            addEvent(l);
                        }
                        Log.w(DEBUG_TAG,"LESSONS RETRIEVED");
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        collapsibleCalendar = view.findViewById(R.id.calendarView);
        recyclerView = view.findViewById(R.id.lezioni_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

//        try {
//            //lezioni = (List<Lezione>) getArguments().getSerializable("lezioniList");
//            Log.w(DEBUG_TAG,lezioniList.toString());
//        }catch (NullPointerException e){
//            Log.w(DEBUG_TAG,"lezioni null");
//        }



        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDaySelect() {
                int day = collapsibleCalendar.getSelectedDay().getDay();
                int month = collapsibleCalendar.getSelectedDay().getMonth();
                int year = collapsibleCalendar.getSelectedDay().getYear();

                List<Lezione> lezioniPerData = new ArrayList<>();

                for(Lezione l: lezioniList) {
                    if(l.getGregorianData().get(Calendar.DAY_OF_MONTH) == day &&
                            l.getGregorianData().get(Calendar.MONTH) == month &&
                            l.getGregorianData().get(Calendar.YEAR) == year){
                        lezioniPerData.add(l);
                    }
                }

                // specify an adapter
                mAdapter = new LezioniAdapter(lezioniPerData);
                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onItemClick(View view) {

            }

            @Override
            public void onDataUpdate() {

            }

            @Override
            public void onMonthChange() {

            }

            @Override
            public void onWeekChange(int i) {

            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
               getLezioni();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }






}
