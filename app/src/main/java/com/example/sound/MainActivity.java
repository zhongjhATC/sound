package com.example.sound;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.zhongjh.soundpoolqueue.MediaPlayerQueue;
import com.zhongjh.soundpoolqueue.SoundPoolPlayer;

public class MainActivity extends AppCompatActivity {

    private AssetManager assetManager; // 资源管理框架

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

        // 播放请继续测温10次
        findViewById(R.id.button1).setOnClickListener(v -> {
            for (int i = 0; i < 10; i++) {
                mediaPlayerQueue.play(VoicePromptType.NEAR_MEASURE);
            }

        });

        findViewById(R.id.button2).setOnClickListener(v -> {
            mediaPlayerQueue.play(VoicePromptType.PASS);
        });
//
//        findViewById(R.id.button3).setOnClickListener(v -> {
//            AssetManager assetManager = App.getInstance().getAssets();
//            AssetFileDescriptor afd = null;
//            try {
//                afd = assetManager.openFd("sound/locale/zh_CN/near_measure_temperature.mp3");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            SoundPoolPlayer soundPoolPlayer = new SoundPoolPlayer.Builder().maxDuration(2000).create(afd);
//            soundPoolPlayer.play();
//            soundPoolPlayer.setOnCompletionListener(mp -> soundPoolPlayer.play());
//
////            SoundPoolPlayer2 soundPoolPlayer = SoundPoolPlayer2.create(App.getInstance(),R.raw.near_measure_temperature);
////            soundPoolPlayer.play();
////            soundPoolPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
////                @Override
////                public void onCompletion(MediaPlayer mp) {
////                    soundPoolPlayer.play();
////                }
////            });
//
//        });

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
