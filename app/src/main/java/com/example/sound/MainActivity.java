package com.example.sound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;

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

        findViewById(R.id.button3).setOnClickListener(v -> {
            AssetManager assetManager = App.getInstance().getAssets();
            AssetFileDescriptor afd = null;
            try {
                afd = assetManager.openFd("sound/locale/zh_CN/near_measure_temperature.mp3");
            } catch (IOException e) {
                e.printStackTrace();
            }
            SoundPoolPlayer soundPoolPlayer = SoundPoolPlayer.create(afd);
            soundPoolPlayer.play();
            soundPoolPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    soundPoolPlayer.play();
                }
            });

//            SoundPoolPlayer2 soundPoolPlayer = SoundPoolPlayer2.create(App.getInstance(),R.raw.near_measure_temperature);
//            soundPoolPlayer.play();
//            soundPoolPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    soundPoolPlayer.play();
//                }
//            });

        });

    }


}
