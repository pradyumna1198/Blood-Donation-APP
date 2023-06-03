package com.example.blood_donation_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash_Screen_Activity extends AppCompatActivity {
    //splash screen includes an into-app motion at launch , a splash screen showing your app icon and a
    //transition  to your app itself .

    private ImageView logo;
    private TextView title,slogan;


    Animation topAnimation,bottomAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow() .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);

        logo = findViewById(R.id.logo);
        title = findViewById(R.id.title);
        slogan = findViewById(R.id.slogan);

        topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        logo.setAnimation(topAnimation);
        title.setAnimation(bottomAnimation);
        slogan.setAnimation(bottomAnimation);


        int SPLASH_SCREEN = 4300;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {  //by using intent you can start a new instance of an Activity by passing an intent to startActivity()
                // the intent describes the activity to start and carries any necessary data
                Intent intent = new Intent(Splash_Screen_Activity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);


    }
}