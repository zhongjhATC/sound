package com.example.sound;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.zhongjh.soundpoolqueue.MediaPlayerQueue;
import com.zhongjh.soundpoolqueue.SoundPoolPlayer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // 资源管理框架
    private AssetManager assetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assetManager = MainActivity.this.getAssets();

        MediaPlayerQueue mediaPlayerQueue = new MediaPlayerQueue();
        mediaPlayerQueue.addSoundPoolPlayer(VoicePromptType.PASS, loadSound("pass.mp3"));
        mediaPlayerQueue.addSoundPoolPlayer(VoicePromptType.UNAUTHORIZED, loadSound("no_auth.mp3"));
        mediaPlayerQueue.addSoundPoolPlayer(VoicePromptType.NO_MASK, loadSound("no_mask.mp3"));
        mediaPlayerQueue.addSoundPoolPlayer(VoicePromptType.NORMAL_TEMPERATURE, loadSound("temp_normal.mp3"));
        mediaPlayerQueue.addSoundPoolPlayer(VoicePromptType.ABNORMAL_TEMPERATURE, loadSound("temp_exception.mp3"));
        mediaPlayerQueue.addSoundPoolPlayer(VoicePromptType.NEAR_MEASURE, loadSound("near_measure_temperature.mp3"));
        // 请靠近测温允许重复播放
        ArrayList<Integer> repetitions = new ArrayList<>();
        repetitions.add(VoicePromptType.NEAR_MEASURE);
        mediaPlayerQueue.setRepetitions(repetitions);

        // 播放请靠近测温10次
        findViewById(R.id.button1).setOnClickListener(v -> {
            for (int i = 0; i < 10; i++) {
                mediaPlayerQueue.play(VoicePromptType.NEAR_MEASURE);
            }

        });

        // 播放温度正常，播放pass
        findViewById(R.id.button2).setOnClickListener(v -> {
            mediaPlayerQueue.play(VoicePromptType.NORMAL_TEMPERATURE);
            mediaPlayerQueue.play(VoicePromptType.PASS);
        });

        // 立即停止所有
        findViewById(R.id.button3).setOnClickListener(v -> mediaPlayerQueue.clearAll(true));

        // 停止所有（除了当前正在播放的）
        findViewById(R.id.button4).setOnClickListener(v -> mediaPlayerQueue.clearAll(false));

        // 立即停止所有 靠近测温的
        findViewById(R.id.button5).setOnClickListener(v -> mediaPlayerQueue.clearAllSpecifyType(VoicePromptType.NEAR_MEASURE));
    }

    /**
     * 加载提示语音
     */
    public SoundPoolPlayer loadSound(String fileName) {
        try {
            AssetFileDescriptor assetFileDescriptor = assetManager.openFd("sound/locale/zh_CN/" + fileName);
            return new SoundPoolPlayer.Builder().maxDuration(2000).create(assetFileDescriptor);
        } catch (Exception e) {
            Log.e("MediaPlayerUtils", "load sound error", e);
        }
        return null;
    }

}
