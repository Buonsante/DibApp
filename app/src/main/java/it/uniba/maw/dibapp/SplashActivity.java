package it.uniba.maw.dibapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getLezioni();
        prova_logo=(ImageView) findViewById(R.id.prova_logo);
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.mytransition);
        prova_logo.startAnimation(myanim);

        myanim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                FirebaseUser user;
                user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {
                    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(SplashActivity.this);
                    Bundle bundle = new Bundle();
                    bundle.putString("user_display_name", "opened by " + user.getDisplayName());
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);

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

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void getLezioni(){
        lezioniList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query query;
        if(getSharedPreferences(SHARED_PREFERENCE_NAME,MODE_PRIVATE).getString("tipo","").equals("D"))
            query = db.collectionGroup("lezioni").whereEqualTo("professore","Denaro Roberto");
        else
            query = db.collectionGroup("lezioni");

        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.w(DEBUG_TAG,"Retrieve Lezioni");
                        for(DocumentSnapshot document : queryDocumentSnapshots.getDocuments()){
                            Lezione l = document.toObject(Lezione.class);
                            l.setLinkLezione(document.getReference().getPath());
                            //Log.w(DEBUG_TAG,"Lezione: "+l.toString());
                            Util.lezioniList.add(l);
                        }
                        Log.w(DEBUG_TAG,"LESSONS RETRIEVED");
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(DEBUG_TAG+"err",e.getMessage());
                    }
                });
    }
}
