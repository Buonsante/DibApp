package it.uniba.maw.dibapp.fragment;


import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.uniba.maw.dibapp.R;
import it.uniba.maw.dibapp.model.Lezione;

public class LezioniAdapter extends RecyclerView.Adapter<LezioniAdapter.LezioniViewHolder>{

    private List<Lezione> lezioni;

    public LezioniAdapter(List<Lezione> lezioni){
        this.lezioni = lezioni;
    }

    @NonNull
    @Override
    public LezioniViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lezione_adapter_row, parent, false);

        LezioniViewHolder vh = new LezioniViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull LezioniViewHolder lezioniViewHolder, int i) {
        lezioniViewHolder.textView.setText(lezioni.get(i).getInsegnamento());
    }

    @Override
    public int getItemCount() {
        return lezioni.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class LezioniViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;

        public LezioniViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }

}
