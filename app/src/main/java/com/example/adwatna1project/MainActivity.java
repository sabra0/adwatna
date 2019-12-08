package com.example.adwatna1project;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
private static int SPLASH_TIME_OUT=4000; //time of splash screen: 4 seconds
ImageView gif ;
private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo= findViewById(R.id.logo);
        gif=findViewById(R.id.gif);

        new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                  Intent intent=new Intent(MainActivity.this, WelcomeActivity.class);
                  startActivity(intent);
                  finish();
              }
          }
                ,SPLASH_TIME_OUT
        );

//to make animation while app is opening
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mysplashanimation);
        logo.startAnimation(myanim);


// to make gif not like jus an image
        Glide.with(getApplicationContext())
                .asGif()
                .load(R.drawable.cogwheel)
                .into(gif);
    }
}