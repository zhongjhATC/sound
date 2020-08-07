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

    private static final String TAG = MediaPlayerQueue.class.getSimpleName();
    private HashMap<Integer, SoundPoolPlayer> soundList = new HashMap<>(); // 用HashMap方式存储类型
    private HashMap<Integer, Boolean> soundIsPlayList = new HashMap<>(); // 用HashMap方式存储类型,这个判断类型是否播放中
    private ArrayList<Integer> playerQueues = new ArrayList<>(); // 加入播放的音乐队列
    private ArrayList<Integer> repetitions = new ArrayList<>(); // 允许重复播放的音乐类型
    private boolean isPlay = false; // 这个队列正在播放

    /**
     * 加入类型
     */
    public void addSoundPoolPlayer(int type, SoundPoolPlayer soundPoolQueue) {
        soundList.put(type, soundPoolQueue);
        soundIsPlayList.put(type, false);

        // 播放完的事件
        Objects.requireNonNull(soundList.get(type)).setOnCompletionListener(mp -> playCompletionListener(type));
    }

    /**
     * @param repetitions 重复播放的音乐类型
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
        if (soundList.get(type) == null) {
            // 抛出异常
            return;
        }
        if (!repetitions.contains(type)) {
            // 是否在队列中已经播放
            if (Objects.requireNonNull(soundIsPlayList.get(type))) {
                return;
            } else {
                Objects.requireNonNull(soundIsPlayList.put(type, true));
            }
        }
        playerQueues.add(type);
        if (!isPlay) {
            playRecursive();
        }
    }

    /**
     * 递归播放
     */
    private void playRecursive() {
        // 音效队列已经没有了
        if (playerQueues.size() <= 0) {
            isPlay = false;
            return;
        }
        // 开始播放
        isPlay = true;
        Log.d(TAG, "总共" + playerQueues.size() + "份语音 :" + playerQueues.get(0));

        // 如果这个队列没有音频文件，则播放下一个
        if (soundList.get(playerQueues.get(0)) == null) {
            // 播放下一首
            playCompletionListener(playerQueues.get(0));
        } else {
            // 开始播放
            Objects.requireNonNull(soundList.get(playerQueues.get(0))).play();
        }
    }

    /**
     * 播放完后的动作
     * 1.播放下一首
     * 2.设置播放结束
     */
    private void playCompletionListener(int type) {
        if (playerQueues.size() > 0) {
            if (type == playerQueues.get(0)) {
                // 播放完，判断类型是否一样，一样就删除本身
                Log.d(TAG, "删除语音 :" + playerQueues.get(0));
                playerQueues.remove(0);
            }
            // 播放下一首
            playRecursive();
        } else {
            isPlay = false;
        }
    }

    /**
     * 删除所有某个类型的音乐类型
     */
    public void clearAllSpecifyType(int type) {
        Iterator<Integer> it = playerQueues.iterator();
        while (it.hasNext()) {
            Integer x = it.next();
            if (x == type)
                it.remove();
        }
        if (playerQueues.size() <= 0) {
            isPlay = false;
        }
    }

    /**
     * @param isPrerupt 是否停止所有语音的时候突然中断当前声音
     */
    public void clearAll(boolean isPrerupt) {
        Log.d(TAG, "clearAll");
        if (isPrerupt) {
            // 设置当前状态不是播放中
            isPlay = false;
            // 停止当前所有声音队列
            for (Integer item : playerQueues) {
                if (soundList.get(item) != null)
                    Objects.requireNonNull(soundList.get(item)).stop();
            }
            // 删除所有声音队列
            playerQueues.clear();
        } else {
            // 如果是播放中，则留下当前第一个，等它播放完
            for (int i = playerQueues.size() - 1; i > 0; i--) {
                if (playerQueues.get(i) != null)
                    playerQueues.remove(i);
            }
        }
        // 设置所有音乐类型都不是播放中
        for (Map.Entry<Integer, Boolean> entry : soundIsPlayList.entrySet()) {
            entry.setValue(false);
        }
    }

}
