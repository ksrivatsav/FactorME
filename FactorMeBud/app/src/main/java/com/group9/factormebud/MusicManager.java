package com.group9.factormebud;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.HashMap;

/**
 * Created by NaveenJetty on 11/4/2015.
 */
public class MusicManager {
    private static final String TAG = "MusicManager";
    private static int musicPlaying = -1;
    public static final int music = 0;
    private static HashMap players = new HashMap();

    public static void start(Context context) {
        start(context, false);
    }

    public static void start(Context context, boolean force) {
        if(!force && musicPlaying>-1){
            return;
        }
        MediaPlayer mp = (MediaPlayer) players.get(music);
        if (mp != null) {
            if (!mp.isPlaying()) {
                mp.start();
                musicPlaying = 0;
            }
        }else {
            mp = MediaPlayer.create(context, R.raw.gamemusic);

            if (mp == null) {
                //Music Player didnt get created properly
                return;
            } else {
                if (!mp.isPlaying()) {
                    mp.setLooping(true);
                    mp.start();
                    musicPlaying = 0;
                    players.put(music, mp);
                }
            }
        }
    }

    public static void pause() {
        MediaPlayer mp = (MediaPlayer) players.get(0);
        if (mp.isPlaying()){
            mp.pause();
            musicPlaying = -1;
        }
    }

    public static void release() {
        MediaPlayer mp = (MediaPlayer) players.get(0);
        if (mp!=null) {
            if (mp.isPlaying()) {
                mp.stop();
            }
            mp.release();
            players.clear();
        }
    }
}
