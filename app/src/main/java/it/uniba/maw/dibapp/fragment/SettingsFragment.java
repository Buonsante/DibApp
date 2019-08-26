package it.uniba.maw.dibapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import it.uniba.maw.dibapp.R;
import it.uniba.maw.dibapp.util.Util;

public class SettingsFragment extends PreferenceFragmentCompat {

    public SwitchPreference not_pref;
    private static String MYIMP = "preferenceDefault";
//    private final static String PREF_TEXT = "prefText";

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        // Load the Preferences from the XML file
        addPreferencesFromResource(R.xml.app_preferences);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(MYIMP, Context.MODE_PRIVATE);

        not_pref = (SwitchPreference) findPreference("key1");

        if (sharedPreferences.getBoolean(not_pref.getKey(), true)) {

            Util.createNotification(getContext());
//            Intent setNotification = new Intent(getActivity(), Util.class);
//            // passo all'attivazione delle notifiche nella classe NotificationUtils
//            startActivity(setNotification);
        }



    }
}