package it.uniba.maw.dibapp.fragment;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import it.uniba.maw.dibapp.LessonActivity;
import it.uniba.maw.dibapp.R;
import it.uniba.maw.dibapp.model.Lezione;

import static it.uniba.maw.dibapp.util.Util.DEBUG_TAG;

public class LezioniAdapter extends RecyclerView.Adapter<LezioniAdapter.LezioniViewHolder>{

    private List<Lezione> lezioni;

    private Context context;

    public LezioniAdapter(List<Lezione> lezioni){
        this.lezioni = lezioni;
    }

    @NonNull
    @Override
    public LezioniViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lezione_adapter_row, parent, false);

        LezioniViewHolder vh = new LezioniViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull LezioniViewHolder lezioniViewHolder, int i) {
        lezioniViewHolder.textViewLessonTitle.setText(lezioni.get(i).getInsegnamento());
        lezioniViewHolder.textViewLessonTeacher.setText(lezioni.get(i).getProfessore());

        lezioniViewHolder.textViewLessonTimeStart.setText(formattaOra(lezioni.get(i).getOraInizio()));
        lezioniViewHolder.textViewLessonTimeFinish.setText(formattaOra(lezioni.get(i).getOraFine()));
    }

    private String formattaOra(String ora){
        if(ora.length() == 1)
            return "0"+ora+":00";
        else
            return ora+":00";
    }

    @Override
    public int getItemCount() {
        return lezioni.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class LezioniViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public MaterialCardView materialCardView;
        public TextView textViewLessonTitle;
        public TextView textViewLessonTeacher;
        public TextView textViewLessonTimeStart;
        public TextView textViewLessonTimeFinish;

        public LezioniViewHolder(View v) {
            super(v);
            materialCardView = v.findViewById(R.id.materialCardLesson);
            textViewLessonTitle = v.findViewById(R.id.textViewLessonTitle);
            textViewLessonTeacher = v.findViewById(R.id.textViewLessonTeacher);
            textViewLessonTimeStart = v.findViewById(R.id.textViewLessonTimeStart);
            textViewLessonTimeFinish = v.findViewById(R.id.textViewLessonTimeFinish);

            materialCardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Log.w(DEBUG_TAG+"t",lezioni.get(position).toString());
            startDetailActivity(lezioni.get(position));

        }

        void startDetailActivity(Lezione lezione){
            Intent intent = new Intent(context, LessonActivity.class);
            intent.putExtra("lezione", lezione);
            context.startActivity(intent);
        }
    }




}
