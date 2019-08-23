package it.uniba.maw.dibapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import it.uniba.maw.dibapp.R;
import it.uniba.maw.dibapp.model.Lezione;

import static it.uniba.maw.dibapp.util.Util.DEBUG_TAG;


public class CalendarFragment extends Fragment {

    private CollapsibleCalendar collapsibleCalendar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;



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
            String str = getArguments().getString("stringa");
            Log.w(DEBUG_TAG, str);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);




        collapsibleCalendar = view.findViewById(R.id.calendarView);
        recyclerView = view.findViewById(R.id.lezioni_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        Lezione lezione = new Lezione();
        List<Lezione> lezioni = new ArrayList<>();

        lezioni = lezione.getLezioniProva();

        for(Lezione l: lezioni) {
            collapsibleCalendar.addEventTag(l.getData().get(Calendar.YEAR), l.getData().get(Calendar.MONTH), l.getData().get(Calendar.DAY_OF_MONTH));
        }


//        collapsibleCalendar.addEventTag(lezioni.get(3).getData().get(Calendar.YEAR), lezioni.get(3).getData().get(Calendar.MONTH),
//                                        lezioni.get(3).getData().get(Calendar.DAY_OF_MONTH));


        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDaySelect() {
                int day = collapsibleCalendar.getSelectedDay().getDay();
                int month = collapsibleCalendar.getSelectedDay().getMonth();
                int year = collapsibleCalendar.getSelectedDay().getYear();

                Lezione lezione = new Lezione();
                List<Lezione> lezioni = new ArrayList<>();
                List<Lezione> lezioniPerData = new ArrayList<>();

                lezioni = lezione.getLezioniProva();

                for(Lezione l: lezioni) {
                    if(l.getData().get(Calendar.DAY_OF_MONTH) == day &&
                            l.getData().get(Calendar.MONTH) == month &&
                            l.getData().get(Calendar.YEAR) == year){
                        lezioniPerData.add(l);
                    }
                }

                Log.w("List", lezioni.toString());

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

        // Inflate the layout for this fragment
        return view;
    }



}
