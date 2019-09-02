package it.uniba.maw.dibapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.uniba.maw.dibapp.R;
import it.uniba.maw.dibapp.model.Lezione;
import it.uniba.maw.dibapp.model.Valutazione;

import static it.uniba.maw.dibapp.util.Util.DEBUG_TAG;


public class CommentiFragment extends Fragment {

    private Lezione lezione;

    private List<Valutazione> valutazioni = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    public CommentiFragment() {
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
        View view = inflater.inflate(R.layout.fragment_commenti, container, false);


        recyclerView = view.findViewById(R.id.commento_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        lezione = (Lezione) getArguments().getSerializable("lezione");

       //TODO recupero commenti db
        // commenti prova
        Log.w(DEBUG_TAG, "caricamento commenti");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(lezione.getLinkLezione()+"/commenti").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot d : queryDocumentSnapshots.getDocuments()) {
                    Log.w(DEBUG_TAG, d.toString());
                    valutazioni.add(new Valutazione(Float.parseFloat(d.getString("rating")), 0, d.getString("commento"), true, null, null));

                }
                mAdapter.notifyDataSetChanged();
            }
        });

        // specify an adapter
        mAdapter = new CommentiAdapter(valutazioni);
        recyclerView.setAdapter(mAdapter);

        return view;
    }


}
