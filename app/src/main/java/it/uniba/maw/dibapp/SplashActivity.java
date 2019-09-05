package it.uniba.maw.dibapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private ImageView prova_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
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
                    startActivity(intent);

                } else {
                    // No user is signed in
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
