package it.uniba.maw.dibapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

import java.text.ParseException;
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

        Lezione lezione = new Lezione();
        List<Lezione> lezioni = new ArrayList<>();

        lezioni = lezione.getLezioniProva();

        Log.w("List", lezioni.toString());


        collapsibleCalendar = view.findViewById(R.id.calendarView);
        recyclerView = view.findViewById(R.id.lezioni_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        mAdapter = new LezioniAdapter(lezioni);
        recyclerView.setAdapter(mAdapter);


        collapsibleCalendar.addEventTag(lezioni.get(3).getData().get(Calendar.YEAR), lezioni.get(3).getData().get(Calendar.MONTH),
                                        lezioni.get(3).getData().get(Calendar.DAY_OF_MONTH));


        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDaySelect() {

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
