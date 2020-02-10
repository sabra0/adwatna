package com.example.adwatna1project;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=4000; //time of splash screen: 4 seconds
    ImageView gif ;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo= findViewById(R.id.logo);
        gif=findViewById(R.id.gif);

//function for splash screen
        new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                  Intent intent=new Intent(SplashActivity.this, WelcomePage.class);
                  startActivity(intent);
                  finish();
              }
          }
                ,SPLASH_TIME_OUT
        );

//to make animation for logo imageView while app is opening
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mysplashanimation);
        logo.startAnimation(myanim);

// to make gif not like just an image
        Glide.with(getApplicationContext())
                .asGif()
                .load(R.drawable.cogwheel)
                .into(gif);
    }
}