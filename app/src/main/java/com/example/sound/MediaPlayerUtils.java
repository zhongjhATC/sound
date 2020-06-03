package com.example.sound;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * mediaPlayer工具
 * Created by siren on 2018/3/6.
 * https://blog.csdn.net/monkin2011/article/details/79109149
 * https://github.com/khliu1238/SoundPoolPlayer/blob/master/SoundPoolPlayer.java
 */

public class MediaPlayerUtils {

    private static final String TAG = "MediaPlayerUtils";

    private static SoundPool mSoundPoll;

    private static int passId;

    private static int noAuthId;

    private static int tempNormalId;

    private static int tempExceptionId;

    private static int nearMeasureTemperatureId;
    private static ArrayList<Integer> nearMeasureTemperatureIds = new ArrayList<>();

    private static int noMaskId;

    private static long lastPlayTime;

    private static Handler mHandler = new Handler();

    private static AssetManager assetManager;

    public static void init() {
        //获取语言路径
        assetManager = App.getInstance().getAssets();

        //设置描述音频流信息的属性
        AudioAttributes abs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        mSoundPoll = new SoundPool.Builder()
                .setMaxStreams(3)   //设置允许同时播放的流的最大值
                .setAudioAttributes(abs)   //完全可以设置为null
                .build();

        try {

            //请通行
            AssetFileDescriptor afd = assetManager.openFd("sound/locale/zh_CN/pass.mp3");
            passId = mSoundPoll.load(afd, 9);

            //未带口罩
            afd = assetManager.openFd("sound/locale/zh_CN/no_mask.mp3");
            noMaskId = mSoundPoll.load(afd, 9);

            //未授权
            afd = assetManager.openFd("sound/locale/zh_CN/no_auth.mp3");
            noAuthId = mSoundPoll.load(afd, 9);

            //体温正常
            afd = assetManager.openFd("sound/locale/zh_CN/temp_normal.mp3");
            tempNormalId = mSoundPoll.load(afd, 9);

            //体温异常
            afd = assetManager.openFd("sound/locale/zh_CN/temp_exception.mp3");
            tempExceptionId = mSoundPoll.load(afd, 9);

            //靠近测温语音
            afd = assetManager.openFd("sound/locale/zh_CN/near_measure_temperature.mp3");
            nearMeasureTemperatureId = mSoundPoll.load(afd, 9);

        } catch (Exception e) {
            Log.e("MediaPlayerUtils", "load sound error", e);
        }

    }

    public static void playPass() {
        stopAllNearMeasureTemperature();
        playImmediately(passId);
    }

    public static void playTempNormal() {
        stopAllNearMeasureTemperature();
        showSound(tempNormalId);
    }

    public static void playTempException() {
        stopAllNearMeasureTemperature();
        showSound(tempExceptionId);
    }

    public static void playNoMask() {
        stopAllNearMeasureTemperature();
        showSound(noMaskId);
    }

    public static void playNoAuth() {
        stopAllNearMeasureTemperature();
        showSound(noAuthId);
    }

    /**
     * 播放请靠近测温
     */
    public static void playNearMeasureTemperature() {
        showSound(nearMeasureTemperatureId);
    }

    /**
     * 停止播放所有的靠近测温
     */
    private static void stopAllNearMeasureTemperature() {
        for (Integer nearId : nearMeasureTemperatureIds) {
            mSoundPoll.stop(nearId);
        }
    }

    private static void showSound(int soundId) {
//        long intervalTime = System.currentTimeMillis() - lastPlayTime;
//
//        if (intervalTime < 2000) {
//            long delay = 2000 - intervalTime;
//            mHandler.postDelayed(() -> showSound(soundId), delay);
//            return;
//        }
//
//        lastPlayTime = System.currentTimeMillis();
        int playId = mSoundPoll.play(soundId, 1, 1, 9, 0, 1f);
        if (nearMeasureTemperatureId == soundId)
            nearMeasureTemperatureIds.add(playId);
    }

    private static void playImmediately(int soundId) {
        mSoundPoll.play(soundId, 1, 1, 9, 0, 1f);
    }

    private void test() {
        Observable<Integer> a = Observable.just(4, 5, 6)
                .startWith(0)  // 追加单个数据 = startWith()
                .startWithArray(1, 2, 3); // 追加多个数据 = startWithArray()
        a.startWith(0);
        a.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "接收到了事件" + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "对Error事件作出响应");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "对Complete事件作出响应");
            }
        });

    }


}