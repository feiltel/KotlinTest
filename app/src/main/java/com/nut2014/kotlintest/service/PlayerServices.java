package com.nut2014.kotlintest.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

/**
 * 播放音乐服务
 */
public class PlayerServices extends Service {
    private static final String TAG = "PlayerServices";

    private MediaPlayer player;

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.d(TAG, "onBufferingUpdate" + percent);
            }
        });

        Log.d(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
            player.reset();
            player.release();
        }
        Log.d(TAG, "onDestroy");
    }

    public class MyBinder extends Binder {
        public void prepare(String path) {
            try {
                player.setDataSource(path);
                //准备资源
                player.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "准备播放音乐");
        }

        //判断是否处于播放状态
        public boolean isPlaying() {
            return player.isPlaying();
        }

        //播放或暂停歌曲
        public void pause() {
            Log.d(TAG, "pause");
            if (player.isPlaying()) {
                player.pause();
            }
        }

        public void stop() {
            Log.d(TAG, "stop");
            if (player.isPlaying()) {
                player.stop();
            }
        }

        public void play() {
            Log.d(TAG, "play");
            if (!player.isPlaying()) {
                player.start();
            }
        }


        //返回歌曲的长度，单位为毫秒
        public int getDuration() {
            return player.getDuration();
        }

        //返回歌曲目前的进度，单位为毫秒
        public int getCurrentPosition() {
            return player.getCurrentPosition();
        }

        //设置歌曲播放的进度，单位为毫秒
        public void seekTo(int mesc) {
            player.seekTo(mesc);
        }
    }
}

