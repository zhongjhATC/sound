package com.example.sound;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;


public class SoundPoolPlayer extends SoundPool {

    private AssetFileDescriptor assetFileDescriptor; // 播放文件
    private int soundId; // 播放id
    private int streamId; // 流id,这个id才是用来暂停等操作的
    private long duration = 0; // 播放时长
    private boolean isPlaying = false; // 是否正在播放中
    private boolean loaded = false; // 是否加载中
    private Handler handler;
    private long startTime; // 播放时间
    private long timeSinceStart = 0; // 暂停时间
    private MediaPlayer.OnCompletionListener listener; // 完成事件

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying) {
                isPlaying = false;
                Log.d("debug", "ending..");
                if (listener != null) {
                    listener.onCompletion(null);
                }
            }
        }
    };

    public void pause() {
        if (streamId > 0) {
            long endTime = System.currentTimeMillis();
            timeSinceStart += endTime - startTime;
            super.pause(streamId);
            if (handler != null) {
                handler.removeCallbacks(runnable);
            }
            isPlaying = false;
        }
    }

    public void stop() {
        if (streamId > 0) {
            timeSinceStart = 0;
            super.stop(streamId);
            if (handler != null) {
                handler.removeCallbacks(runnable);
            }
            isPlaying = false;
        }
    }

    public void play() {
        if (!loaded) {
            loadAndPlay();
        } else {
            playIt();
        }
    }

    public static SoundPoolPlayer create(AssetFileDescriptor assetFileDescriptor) {
        SoundPoolPlayer player = new SoundPoolPlayer(1, AudioManager.STREAM_MUSIC, 0);
        player.assetFileDescriptor = assetFileDescriptor;
        return player;
    }

    public SoundPoolPlayer(int maxStreams, int streamType, int srcQuality) {
        super(1, streamType, srcQuality);
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        this.listener = listener;
    }

    private void loadAndPlay() {
        duration = getSoundDuration();
        soundId = super.load(assetFileDescriptor, 9);
        setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            loaded = true;
            playIt();
        });
    }

    private void playIt() {
        if (loaded && !isPlaying) {
            Log.d("debug", "start playing..");
            if (timeSinceStart == 0) {
                streamId = super.play(soundId, 1f, 1f, 1, 0, 1f);
            } else {
                super.resume(streamId);
            }
            startTime = System.currentTimeMillis();
            handler = new Handler();
            handler.postDelayed(runnable, duration - timeSinceStart); // 延时处理触发
            isPlaying = true;
        }
    }

    /**
     *
     * @return 获取时长
     */
    private long getSoundDuration() {
        try {
            MediaPlayer player = new MediaPlayer();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                player.setDataSource(assetFileDescriptor);
                player.prepare();
            }
            duration = player.getDuration();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return duration;
    }
}