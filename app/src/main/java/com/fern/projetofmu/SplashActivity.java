package com.fern.projetofmu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.ActionBar;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        // Get the ActionBar (may be null)
        ActionBar actionBar = getSupportActionBar();

        // Hide the ActionBar only if it's not null
        if (actionBar != null) {
            actionBar.hide();
        }
        //or use the safe call operator like in the other example:
        //getSupportActionBar()?.hide()

        final Intent i =new Intent(SplashActivity.this,MainActivity.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run(){
                startActivity(i);
                finish();
            }

        }, 1000);
    }
}