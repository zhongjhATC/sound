package com.zhongjh.soundpoolqueue;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * MediaPlayerQueue 队列工具
 */
public class MediaPlayerQueue {

    public static final String TAG = MediaPlayerQueue.class.getSimpleName();

    private AssetManager assetManager; // 资源管理框架
    private Map<Integer, SoundPoolPlayer> soundList = new HashMap<>(); // 用HashMap方式存储类型
    private ArrayList<Integer> playerModels = new ArrayList<>(); // 音乐队列
    private boolean isPlay = false; // 这个队列正在播放


    /**
     * 初始化
     */
    public void init(Context context) {
        // 设置描述音频流信息的属性
        assetManager = context.getAssets();
        loadSound(VoicePromptType.pass, "pass.mp3");
        loadSound(VoicePromptType.unauthorized, "no_auth.mp3");
        loadSound(VoicePromptType.no_mask, "no_mask.mp3");
        loadSound(VoicePromptType.normal_temperature, "temp_normal.mp3");
        loadSound(VoicePromptType.abnormal_temperature, "temp_exception.mp3");
        loadSound(VoicePromptType.near_measure, "near_measure_temperature.mp3");
    }

    /**
     * 加载提示语音
     */
    private void loadSound(VoicePromptType voicePromptType, String fileName) {
        try {
            String language = LanguageUtil.getLanguage(); //获取语言
            if (language.equals("zh_TW")) {
                language = "zh_CN";
            }
            AssetFileDescriptor assetFileDescriptor = assetManager.openFd("sound/locale/" + language + "/" + fileName);
            SoundPoolPlayer soundPoolPlayer = SoundPoolPlayer.create(assetFileDescriptor);
            soundList.put(voicePromptType, soundPoolPlayer);
            // 事件
            soundList.get(voicePromptType).setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playCompletionListener(voicePromptType);
                }
            });
//            soundList.get(voicePromptType).setOnCompletionListener(mp -> playCompletionListener());
        } catch (Exception e) {
            Log.e("MediaPlayerUtils", "load sound error", e);
        }
    }

    /**
     * 播放
     *
     * @param voicePromptType 类型
     */
    synchronized void play(VoicePromptType voicePromptType) {
        // 如果开启提示语音
        if (SoundSettings.getSettingSoundSwitch()) {
            switch (voicePromptType) {
                case pass:
                    if (isPass) {
                        return;
                    } else {
                        isPass = true;
                        Log.i(TAG, "isPass: " + isPass);
                    }
                    break;
                case no_mask:
                    if (isNoMask)
                        return;
                    else
                        isNoMask = true;
                    break;
                case abnormal_temperature:
                    if (isAbnormalTemperature)
                        return;
                    else
                        isAbnormalTemperature = true;
                    break;
                case normal_temperature:
                    if (isNormalTemperature)
                        return;
                    else
                        isNormalTemperature = true;
                    break;
                case unauthorized:
                    if (isUnauthorized)
                        return;
                    else
                        isUnauthorized = true;
                    break;
            }

            playerModels.add(new PlayerModel(0L, voicePromptType));
            if (!isPlay) {
                playRecursive();
            }
        }
    }

    /**
     * 递归播放
     */
    private void playRecursive() {
        // 音效队列已经没有了
        if (playerModels.size() <= 0) {
            isPlay = false;
            return;
        }
        // 开始播放
        isPlay = true;
        Log.i(TAG, "总共" + playerModels.size() + "份语音 :" + playerModels.get(0).voicePromptType);

        // 如果这个队列没有音频文件，则播放下一个
        if (soundList.get(playerModels.get(0).voicePromptType) == null) {
            // 等待100毫秒
            playCompletionListener(playerModels.get(0).voicePromptType);
        } else {
            // 开始播放
            soundList.get(playerModels.get(0).voicePromptType).play();
        }
    }

    /**
     * 播放完后的动作
     */
    private void playCompletionListener(VoicePromptType voicePromptType) {
        if (playerModels.size() > 0) {
            if (voicePromptType == playerModels.get(0).voicePromptType) {
                // 播放完，判断类型是否一样，一样就删除本身
                Log.i(TAG, "删除语音 :" + playerModels.get(0).voicePromptType);
                playerModels.remove(0);
            }
            // 播放下一首
            playRecursive();
        } else {
            isPlay = false;
        }
    }

    /**
     * 删除所有请靠近语音
     */
    public void clearAllNearMeasure() {
        Iterator<PlayerModel> it = playerModels.iterator();
        while (it.hasNext()) {
            PlayerModel x = it.next();
            if (x.getVoicePromptType() == VoicePromptType.near_measure)
                it.remove();
        }
        if (playerModels.size() <= 0) {
            isPlay = false;
        }
    }

    /**
     * 删除所有声音
     */
    public void clearAll() {
        Log.i(TAG, "clearAll");
        isPlay = false;
        playerModels.clear();
        isPass = false;
        Log.i(TAG, "isPass: " + isPass);
        isUnauthorized = false;
        isNoMask = false;
        isNormalTemperature = false;
        isAbnormalTemperature = false;
    }

}
