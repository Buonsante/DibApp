package it.uniba.maw.dibapp.fragment;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;


import it.uniba.maw.dibapp.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        // Load the Preferences from the XML file
        addPreferencesFromResource(R.xml.app_preferences);
    }
}
