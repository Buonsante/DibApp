package it.uniba.maw.dibapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import it.uniba.maw.dibapp.model.Lezione;
import it.uniba.maw.dibapp.util.Util;

import static it.uniba.maw.dibapp.util.Util.DEBUG_TAG;
import static it.uniba.maw.dibapp.util.Util.SHARED_PREFERENCE_NAME;
import static it.uniba.maw.dibapp.util.Util.lezioniList;

public class SplashActivity extends AppCompatActivity {

    private ImageView prova_logo;
    SharedPreferences pref;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pref = getSharedPreferences(SHARED_PREFERENCE_NAME,MODE_PRIVATE);

        progressBar = findViewById(R.id.splashDataDownload);
        prova_logo = findViewById(R.id.prova_logo);

        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.mytransition);

        prova_logo.startAnimation(myanim);

        myanim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setIndeterminate(true);
                getLezioni();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void startApp(){
        FirebaseUser user;
        user = FirebaseAuth.getInstance().getCurrentUser();
        boolean firstRun = pref.getBoolean("firstRun",true);
        if(user != null && !firstRun) {
            Log.i(DEBUG_TAG,"USER: "+user.getEmail());
            Bundle bundle = new Bundle();
            bundle.putString("user_display_name", "opened by " + user.getDisplayName());

            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            finishAffinity();
            startActivity(intent);

        } else {
            // No user is signed in
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            finishAffinity();
            startActivity(intent);
        }
    }

    private void getLezioni(){
        progressBar.setIndeterminate(false);
        progressBar.setProgress(0);
        lezioniList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query query;
        if(pref.getString("tipo","").equals("D"))
            query = db.collectionGroup("lezioni").whereEqualTo("professore","Denaro Roberto");
        else
            query = db.collectionGroup("lezioni");

        query.get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    int unitOfProgress = 100/queryDocumentSnapshots.size();

                    Log.w(DEBUG_TAG, "Retrieve Lezioni ");
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Log.w(DEBUG_TAG, "Progress: "+progressBar.getProgress());
                        Lezione l = document.toObject(Lezione.class);
                        l.setLinkLezione(document.getReference().getPath());
                        lezioniList.add(l);

                        progressBar.incrementProgressBy(unitOfProgress);

                    }
                    Log.w(DEBUG_TAG, "LESSONS RETRIEVED");
                    progressBar.incrementProgressBy(unitOfProgress);
                    startApp();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(DEBUG_TAG+"err",e.getMessage());
                }
            });
    }
}
