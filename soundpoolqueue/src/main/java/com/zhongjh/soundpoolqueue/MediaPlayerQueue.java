package com.zhongjh.soundpoolqueue;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;


/**
 * MediaPlayerQueue 队列工具
 */
public class MediaPlayerQueue {

    public static final String TAG = MediaPlayerQueue.class.getSimpleName();
    private Map<Integer, SoundPoolQueue> soundList = new HashMap<>(); // 用HashMap方式存储类型
    private ArrayList<Integer> playerModels = new ArrayList<>(); // 音乐队列
    private ArrayList<Integer> repetitions = new ArrayList<>(); // 重复播放的音乐队列
    private boolean isPlay = false; // 这个队列正在播放

    /**
     * 加入类型
     */
    public void addSoundPoolPlayer(int type, SoundPoolQueue soundPoolQueue) {
        soundList.put(type, soundPoolQueue);
    }

    /**
     * @param repetitions 重复播放的音乐队列
     */
    public void setRepetitions(ArrayList<Integer> repetitions) {
        this.repetitions = repetitions;
    }

    /**
     * 播放
     *
     * @param type 类型
     */
    public synchronized void play(int type) {
        if (soundList.get(type) == null){
            // 抛出异常
        }


        if (!repetitions.contains(type)) {
            // 是否在队列中已经播放
            if (soundList.get(type).isHasPlaying()) {
                return;
            } else {
                soundList.get(type).setHasPlaying(true);
            }
        }

//        switch (type) {
//            case pass:
//                if (isPass) {
//                    return;
//                } else {
//                    isPass = true;
//                    Log.i(TAG, "isPass: " + isPass);
//                }
//                break;
//            case no_mask:
//                if (isNoMask)
//                    return;
//                else
//                    isNoMask = true;
//                break;
//            case abnormal_temperature:
//                if (isAbnormalTemperature)
//                    return;
//                else
//                    isAbnormalTemperature = true;
//                break;
//            case normal_temperature:
//                if (isNormalTemperature)
//                    return;
//                else
//                    isNormalTemperature = true;
//                break;
//            case unauthorized:
//                if (isUnauthorized)
//                    return;
//                else
//                    isUnauthorized = true;
//                break;
//        }

        playerModels.add(new PlayerModel(0L, type));
        if (!isPlay) {
            playRecursive();
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
    private void playCompletionListener(Type type) {
        if (playerModels.size() > 0) {
            if (type == playerModels.get(0).voicePromptType) {
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
            if (x.getVoicePromptType() == Type.near_measure)
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
