package com.android.votriteapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        int SPLASH_DISPLAY_LENGTH = 3000;
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, BallotActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();

                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
