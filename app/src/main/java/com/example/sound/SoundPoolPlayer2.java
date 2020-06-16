package com.example.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.util.Log;

/* SoundPoolPlayer2:
   custom extention from SoundPool with setOnCompletionListener
   without the low-efficiency drawback of MediaPlayer
   author: kenliu
*/
public class SoundPoolPlayer2 extends SoundPool {

    Context context;
    int soundId;
    int streamId;
    int resId;
    long duration;
    boolean isPlaying = false;
    boolean loaded = false;
    MediaPlayer.OnCompletionListener listener;
    Runnable runnable = new Runnable(){
        @Override
        public void run(){
            if(isPlaying){
                isPlaying = false;
                Log.d("debug", "ending..");
                if(listener != null){
                    listener.onCompletion(null);
                }
            }
        }
    };

    //timing related
    Handler handler;
    long startTime;
    long endTime;
    long timeSinceStart = 0;

    /**
     * Constructor. Constructs a SoundPool object with the following
     * characteristics:
     *
     * @param maxStreams the maximum number of simultaneous streams for this
     *                   SoundPool object
     * @param streamType the audio stream type as described in AudioManager
     *                   For example, game applications will normally use
     *                   {@link AudioManager#STREAM_MUSIC}.
     * @param srcQuality the sample-rate converter quality. Currently has no
     *                   effect. Use 0 for the default.
     * @return a SoundPool object, or null if creation failed
     * @deprecated use {@link Builder} instead to create and configure a
     * SoundPool instance
     */
    public SoundPoolPlayer2(int maxStreams, int streamType, int srcQuality) {
        super(maxStreams, streamType, srcQuality);
    }

    public void pause(){
        if(streamId > 0){
            endTime = System.currentTimeMillis();
            timeSinceStart += endTime - startTime;
            super.pause(streamId);
            if(handler != null){
                handler.removeCallbacks(runnable);
            }
            isPlaying = false;
        }
    }

    public void stop(){
        if(streamId > 0){
            timeSinceStart = 0;
            super.stop(streamId);
            if(handler != null){
                handler.removeCallbacks(runnable);
            }
            isPlaying = false;
        }
    }

    public void play(){
        if(!loaded){
            loadAndPlay();
        }
        else{
            playIt();
        }
    }

    public static SoundPoolPlayer2 create(Context context, int resId){
        SoundPoolPlayer2 player = new SoundPoolPlayer2(1, AudioManager.STREAM_MUSIC, 0);
        player.context = context;
        player.resId = resId;
        return player;
    }

    public boolean isPlaying(){
        return isPlaying;
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener){
        this.listener = listener;
    }

    private void loadAndPlay(){
        duration = getSoundDuration(resId);
        soundId = super.load(context, resId, 1);
        setOnLoadCompleteListener(new OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
                playIt();
            }
        });
    }

    private void playIt(){
        if(loaded && !isPlaying){
            Log.d("debug", "start playing..");
            if(timeSinceStart == 0){
                streamId = super.play(soundId, 1f, 1f, 1, 0, 1f);
            }
            else{
                super.resume(streamId);
            }
            startTime = System.currentTimeMillis();
            handler = new Handler();
            handler.postDelayed(runnable, duration - timeSinceStart);
            isPlaying = true;
        }
    }

    private long getSoundDuration(int rawId){
        MediaPlayer player = MediaPlayer.create(context, rawId);
        int duration = player.getDuration();
        return duration;
    }
}