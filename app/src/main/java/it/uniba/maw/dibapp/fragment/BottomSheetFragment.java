package it.uniba.maw.dibapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import it.uniba.maw.dibapp.R;
import it.uniba.maw.dibapp.model.Lezione;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private Lezione lezione;

    EditText editTextCommento;
    Button buttonValuta;
    RatingBar ratingBar;

    public BottomSheetFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);

        editTextCommento = view.findViewById(R.id.edit_text_commento);
        buttonValuta = view.findViewById(R.id.btn_valuta);
        ratingBar = view.findViewById(R.id.rating_bar);


        buttonValuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valutaLezione();
            }
        });

        //recupera lezione da LessonActivity
        lezione = (Lezione) getArguments().getSerializable("lezione");


        return view;
    }


    private void valutaLezione() {
        float rating = ratingBar.getRating();
        String commento = editTextCommento.getText().toString();

        Map<String,String> commentoMap = new HashMap<>();
        commentoMap.put("rating",String.valueOf(rating));
        commentoMap.put("commento",commento);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.document(lezione.getLinkLezione()).collection("commenti").add(commentoMap);

        FragmentManager manager = getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(this);
        trans.commit();

    }
}