package com.nut2014.kotlintest.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

/**
 * @author feiltel 2019/9/30 0030
 */
public class MusicService extends Service {
    private static final String TAG = "MusicService";
    private MediaPlayer player;

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicBinder();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (preparedListener != null) {
                    preparedListener.onPrepared(mp);
                }
            }
        });
        player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                if (updateListener != null) {
                    updateListener.onBufferingUpdate(mp, percent);
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (player != null) {
            player.stop();
            player.reset();
            player.release();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    public MediaPlayer.OnBufferingUpdateListener updateListener;
    public MediaPlayer.OnPreparedListener preparedListener;

    public class MusicBinder extends Binder {

        public void loadData(String path, MediaPlayer.OnPreparedListener onPreparedListener, MediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener) {
            try {
                player.setDataSource(path);
                player.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateListener = onBufferingUpdateListener;
            preparedListener = onPreparedListener;
            Log.d(TAG, "准备播放音乐");
        }

        public boolean isPlaying() {
            return player.isPlaying();
        }

        public void changeStatue() {
            if (player.isPlaying()) {
                player.pause();
            } else {
                player.start();
            }
        }


    }
}
