package com.example.sound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zhongjh.soundpoolqueue.MediaPlayerQueue;
import com.zhongjh.soundpoolqueue.SoundPoolPlayer;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private AssetManager assetManager; // 资源管理框架

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MediaPlayerUtils.init();
        assetManager = MainActivity.this.getAssets();

        MediaPlayerQueue mediaPlayerQueue = new MediaPlayerQueue();
        mediaPlayerQueue.addSoundPoolPlayer(VoicePromptType.PASS,loadSound("pass.mp3"));


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

    /**
     * 加载提示语音
     */
    public SoundPoolPlayer loadSound(String fileName) {
        try {
            AssetFileDescriptor assetFileDescriptor = assetManager.openFd("sound/locale/zh_CN/" + fileName);
            SoundPoolPlayer soundPoolPlayer = new SoundPoolPlayer.Builder().maxDuration(2000).create(assetFileDescriptor);
            return soundPoolPlayer;
        } catch (Exception e) {
            Log.e("MediaPlayerUtils", "load sound error", e);
        }
    }

}
