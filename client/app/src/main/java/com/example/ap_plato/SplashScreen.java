package com.example.ap_plato;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import me.itangqi.waveloadingview.WaveLoadingView;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
         final WaveLoadingView waveLoadingView = findViewById(R.id.waveloading);
        final Handler handler = new Handler();
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                waveLoadingView.setProgressValue(waveLoadingView.getProgressValue()+1);
                handler.postDelayed(this, 150);
                if(waveLoadingView.getProgressValue()>120){
                    handler.removeCallbacks(this);
                    startActivity(new Intent(SplashScreen.this,MainActivity.class));
                    finish();
                }
            }
        };
        handler.post(task);



    }
}