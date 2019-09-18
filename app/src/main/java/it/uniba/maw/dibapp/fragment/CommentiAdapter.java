package it.uniba.maw.dibapp.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import it.uniba.maw.dibapp.model.Valutazione;

import java.util.List;

import it.uniba.maw.dibapp.R;

public class CommentiAdapter extends RecyclerView.Adapter<CommentiAdapter.CommentiViewHolder> {

    private List<Valutazione> commenti;

    private Context context;

    public CommentiAdapter(List<Valutazione> commenti){
        this.commenti = commenti;
    }

    @NonNull
    @Override
    public CommentiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.commento_adapter_row, parent, false);

        CommentiAdapter.CommentiViewHolder vh = new CommentiAdapter.CommentiViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull CommentiViewHolder holder, int position) {
        holder.valutazione.setRating(commenti.get(position).getRating());
        holder.commento.setText(commenti.get(position).getCommento());
        holder.data.setText(commenti.get(position).getData());
//        holder.data.setText(commenti.get(position).getData().toString());
    }

    @Override
    public int getItemCount() {
        return commenti.size();
    }



    public class CommentiViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public MaterialCardView materialCardView;
        public RatingBar valutazione;
        public TextView data;
        public TextView commento;

        public CommentiViewHolder(View v) {
            super(v);
            materialCardView = v.findViewById(R.id.materialCardComment);
            valutazione = v.findViewById(R.id.rating_bar_commento);
            data = v.findViewById(R.id.tv_date);
            commento = v.findViewById(R.id.tv_comment);

        }

    }
}