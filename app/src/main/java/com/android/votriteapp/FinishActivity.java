package com.android.votriteapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.android.votriteapp.utils.Share;
import java.util.ArrayList;

public class FinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        int delay_time = 5000;
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Share.array_race = new ArrayList<>();
                Intent mainIntent = new Intent(FinishActivity.this, BallotActivity.class);
                FinishActivity.this.startActivity(mainIntent);
                FinishActivity.this.finish();

                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        }, delay_time);
    }
}
