package it.uniba.maw.dibapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import it.uniba.maw.dibapp.R;
import it.uniba.maw.dibapp.model.Lezione;
import it.uniba.maw.dibapp.util.Util;

import static it.uniba.maw.dibapp.util.Util.DEBUG_TAG;
import static it.uniba.maw.dibapp.util.Util.lezioniList;


public class LezioniDelGiornoFragment extends Fragment {




    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //textview per allertare l'utente che non ci sono lezioni disponibi nella giornata
    private TextView noLessonsTextView;


    private List<Lezione> lezioniDelGiorno;

    public LezioniDelGiornoFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lezioni_del_giorno, container, false);

        recyclerView = view.findViewById(R.id.lezioni_del_giorno_recycler_view);
        noLessonsTextView = view.findViewById(R.id.noLessonsTodayTextView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        //recupero lezioni del giorno dal database
        lezioniDelGiorno = getLezioniDelGiorno();

        // specify an adapter
        mAdapter = new LezioniAdapter(lezioniDelGiorno);
        recyclerView.setAdapter(mAdapter);

        if(lezioniDelGiorno.isEmpty()){
            noLessonsTextView.setVisibility(View.VISIBLE);
        }else{
            noLessonsTextView.setVisibility(View.GONE);
        }

        return view;
    }

    private List<Lezione> getLezioniDelGiorno() {
        Calendar instance = GregorianCalendar.getInstance();

        int day = instance.get(Calendar.DAY_OF_MONTH);
        int month = instance.get(Calendar.MONTH);
        int year = instance.get(Calendar.YEAR);

        List<Lezione> lezioniDelGiorno = new ArrayList<>();

        for(Lezione l: lezioniList) {
            if(l.getGregorianData().get(Calendar.DAY_OF_MONTH) == day &&
                    l.getGregorianData().get(Calendar.MONTH) == month &&
                    l.getGregorianData().get(Calendar.YEAR) == year){
                lezioniDelGiorno.add(l);
            }
        }

        return lezioniDelGiorno;
    }








}
