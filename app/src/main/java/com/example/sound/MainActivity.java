package com.example.sound;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MediaPlayerUtils.init();

        findViewById(R.id.button1).setOnClickListener(v -> {
            MediaPlayerUtils.playNearMeasureTemperature();
            MediaPlayerUtils.playNearMeasureTemperature();
            MediaPlayerUtils.playNearMeasureTemperature();
            MediaPlayerUtils.playNearMeasureTemperature();
            MediaPlayerUtils.playNearMeasureTemperature();
            MediaPlayerUtils.playNearMeasureTemperature();
            MediaPlayerUtils.playNearMeasureTemperature();
            MediaPlayerUtils.playNearMeasureTemperature();
            MediaPlayerUtils.playNearMeasureTemperature();
            MediaPlayerUtils.playNearMeasureTemperature();
        });

        findViewById(R.id.button2).setOnClickListener(v -> {
            MediaPlayerUtils.playPass();
        });

    }



}
