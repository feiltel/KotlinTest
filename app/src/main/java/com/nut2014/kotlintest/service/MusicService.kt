package com.nut2014.kotlintest.service

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log

import java.io.IOException

/**
 * @author feiltel 2019/9/30 0030
 */
class MusicService : Service() {
    private var player: MediaPlayer? = null

    var updateListener: MediaPlayer.OnBufferingUpdateListener? = null
    var preparedListener: MediaPlayer.OnPreparedListener? = null


    override fun onBind(intent: Intent): IBinder? {
        return MusicBinder()
    }


    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        player = MediaPlayer()
        player!!.setAudioStreamType(AudioManager.STREAM_MUSIC)

        player!!.setOnPreparedListener { mp ->
            if (preparedListener != null) {
                preparedListener!!.onPrepared(mp)
            }
        }
        player!!.setOnBufferingUpdateListener { mp, percent ->
            if (updateListener != null) {
                updateListener!!.onBufferingUpdate(mp, percent)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        closeMedia(player)
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.d(TAG, "onUnbind")
        return super.onUnbind(intent)
    }

    private fun closeMedia(mediaplayer: MediaPlayer?) {
        if (mediaplayer != null) {
            if (mediaplayer.isPlaying) {
                mediaplayer.stop()
            }
            mediaplayer.release()
        }

    }

    inner class MusicBinder : Binder() {

        val isPlaying: Boolean
            get() = player!!.isPlaying

        fun loadData(
            path: String,
            onPreparedListener: MediaPlayer.OnPreparedListener,
            onBufferingUpdateListener: MediaPlayer.OnBufferingUpdateListener
        ) {
            try {
                player!!.setDataSource(path)
                player!!.prepareAsync()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            updateListener = onBufferingUpdateListener
            preparedListener = onPreparedListener
            Log.d(TAG, "准备播放音乐")
        }

        fun pause() {
            if (player != null && player!!.isPlaying) {
                player!!.pause()
            }
        }

        fun start() {
            if (player != null && !player!!.isPlaying) {
                player!!.start()
            }
        }

    }

    companion object {
        private val TAG = "MusicService"
    }
}
