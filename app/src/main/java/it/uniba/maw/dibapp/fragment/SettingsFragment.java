package it.uniba.maw.dibapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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
        not_pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, final Object newValue) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                //modifica l'attributo sendNotification dell'user nel database
                db.collection("utenti").whereEqualTo("mail",user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        queryDocumentSnapshots.getDocuments().get(0).getReference().update("sendNotification", newValue);
                    }
                });
                return true;
            }
        });


    }
}