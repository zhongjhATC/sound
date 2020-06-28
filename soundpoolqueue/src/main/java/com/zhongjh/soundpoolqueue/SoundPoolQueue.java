package com.zhongjh.soundpoolqueue;

public class SoundPoolQueue extends SoundPoolPlayer {

    private boolean hasPlaying = false; // 是否在队列中已经播放

    public boolean isHasPlaying() {
        return hasPlaying;
    }

    public void setHasPlaying(boolean hasPlaying) {
        this.hasPlaying = hasPlaying;
    }



    /**
     * 加载
     *
     * @param maxStreams
     * @param streamType
     * @param srcQuality
     */
    public SoundPoolQueue(int maxStreams, int streamType, int srcQuality) {
        super(maxStreams, streamType, srcQuality);
    }
}
