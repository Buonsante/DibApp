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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    //
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //
    private List<Lezione> lezioniDelGiorno;

    public LezioniDelGiornoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LezioniDelGiornoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LezioniDelGiornoFragment newInstance(String param1, String param2) {
        LezioniDelGiornoFragment fragment = new LezioniDelGiornoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lezioni_del_giorno, container, false);

        recyclerView = view.findViewById(R.id.lezioni_del_giorno_recycler_view);

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
