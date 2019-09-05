package it.uniba.maw.dibapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    private ImageView prova_logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        prova_logo=(ImageView) findViewById(R.id.prova_logo);
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.mytransition);
        prova_logo.startAnimation(myanim);
        Thread timer = new Thread(){
            public void run () {
                try{
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
            timer.start();
    }
}
