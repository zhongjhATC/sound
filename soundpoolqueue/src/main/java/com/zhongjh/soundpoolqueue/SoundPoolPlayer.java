package com.zhongjh.soundpoolqueue;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.util.Log;

/**
 * 这是封装了SoundPool，主要为了能获取播放完后的事件
 */
public class SoundPoolPlayer extends SoundPool {

    private AssetFileDescriptor assetFileDescriptor; // 播放文件，用文件加载的方式生成SoundPool
    private Context context; // 上下文
    private int resId; // 资源id，用资源加载的方式生成SoundPool
    private int soundId; // 播放id
    private int streamId; // 流id,这个id才是用来暂停等操作的
    private long duration = 0; // 播放时长
    private boolean isPlaying = false; // 是否正在播放中
    private boolean loaded = false; // 是否加载中
    private Handler handler;
    private long startTime; // 播放时间
    private long timeSinceStart = 0; // 暂停时间
    private MediaPlayer.OnCompletionListener listener; // 完成事件

    /**
     * 创造者模式
     */
    public static class Builder {

        private int maxDuration; // 由于一些音频文件不规范的特殊因素，比如文件长度有10秒，但是实际上只有8秒是有声音的，所以我们这里要给它设置最长8秒

        /**
         * 用实体方式打开
         *
         * @param maxDuration 最长长度
         * @return 观察者
         */
        public Builder maxDuration(int maxDuration) {
            this.maxDuration = maxDuration;
            return this;
        }

        /**
         * 以AssetFileDescriptor文件方式加载SoundPool
         *
         * @param assetFileDescriptor assetFileDescriptor
         * @return SoundPoolPlayer
         */
        public SoundPoolPlayer create(AssetFileDescriptor assetFileDescriptor) {
            SoundPoolPlayer player = new SoundPoolPlayer(1, AudioManager.STREAM_MUSIC, 0);
            player.assetFileDescriptor = assetFileDescriptor;
            return player;
        }

        /**
         * 以resId资源id方式加载SoundPool
         *
         * @param context 上下文
         * @param resId   资源id
         * @return SoundPoolPlayer
         */
        public SoundPoolPlayer create(Context context, int resId) {
            SoundPoolPlayer player = new SoundPoolPlayer(1, AudioManager.STREAM_MUSIC, 0);
            player.context = context;
            player.resId = resId;
            return player;
        }

    }

    /**
     * 播放流程
     */
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

    /**
     * 播放
     */
    public void play() {
        if (!loaded) {
            loadAndPlay();
        } else {
            playIt();
        }
    }

    /**
     * 暂停
     */
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

    /**
     * 停止
     */
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

    /**
     * 加载
     */
    public SoundPoolPlayer(int maxStreams, int streamType, int srcQuality) {
        super(1, streamType, srcQuality);
    }

    /**
     * @return 是否播放中
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * @param listener 播放完事件
     */
    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        this.listener = listener;
    }

    /**
     * 加载并播放
     */
    private void loadAndPlay() {
        duration = getSoundDuration();
        // 判断是文件方式加载还是资源id方式加载
        if (assetFileDescriptor != null)
            soundId = super.load(assetFileDescriptor, 9);
        else
            // 资源方式加载
            soundId = super.load(context, resId, 9);
        setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            loaded = true;
            playIt();
        });
    }

    /**
     * 播放事件
     */
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
     * @return 获取时长
     */
    private long getSoundDuration() {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        mmr.release();
        duration = Long.parseLong(durationStr);
        if (duration > 2000)
            // 如果超过10秒，则返回2秒
            return 2000;
        return duration;
    }

}
