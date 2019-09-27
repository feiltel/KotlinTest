package com.nut2014.kotlintest.view

import android.animation.ObjectAnimator
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.jaeger.library.StatusBarUtil
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.base.CommonConfig
import com.nut2014.kotlintest.entity.Cover
import com.nut2014.kotlintest.service.PlayerServices
import com.nut2014.kotlintest.utils.GlideImageLoader
import kotlinx.android.synthetic.main.activity_info.*
import java.util.*


class InfoActivity : AppCompatActivity() {
    private var datas: Cover? = null

    private var conn: MyConnection? = null
    private var musicControl: PlayerServices.MyBinder? = null
    private val UPDATE_PROGRESS = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setTransparent(this)
        setContentView(R.layout.activity_info)
        getDataSet()
        initBanner()
        initMusic()

        cover_music_fl.setOnClickListener {
            play()
        }
    }

    private fun initMusic() {
        val intent3 = Intent(this, PlayerServices::class.java)
        conn = MyConnection()
        startService(intent3)
        //使用混合的方法开启服务，
        bindService(intent3, conn, Context.BIND_AUTO_CREATE)
        val path = "mnt/sdcard/test.mp3"
        loadCover(path)
    }

    private fun getDataSet() {
        datas = CommonConfig.fromJson(intent.getStringExtra("cover"), Cover::class.java)
        content_tv.setText(datas!!.coverDes)
    }

    /**
     * 初始化
     */
    private fun initBanner() {
        list_rv.setImageLoader(GlideImageLoader())
        val paths = ArrayList<String>()
        paths.add(datas!!.coverImgPath)
        paths.add(datas!!.coverImgPath)
        paths.add(datas!!.coverImgPath)
        list_rv!!.setImages(paths)
        list_rv.start()
    }

    private inner class MyConnection : ServiceConnection {

        //服务启动完成后会进入到这个方法
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            //获得service中的MyBinder
            musicControl = service as PlayerServices.MyBinder
            //更新按钮的文字
            updatePlayText()
            val progress = musicControl!!.getCurrenPostion() * 1.0f / musicControl!!.getDuration() * 1.0f
            progress_circular.setPercentage(progress)

        }

        override fun onServiceDisconnected(name: ComponentName) {

        }
    }

    private fun loadCover(path: String) {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(path)
        val cover = mediaMetadataRetriever.embeddedPicture
        val bitmap = BitmapFactory.decodeByteArray(cover, 0, cover.size)
        cover_music_im.setImageBitmap(bitmap)
    }


    //使用handler定时更新进度条
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                UPDATE_PROGRESS -> updateProgress()
            }
        }
    }

    //更新进度条
    private fun updateProgress() {
        val progress = musicControl!!.getCurrenPostion() * 1.0f / musicControl!!.getDuration() * 1.0f
        progress_circular.setPercentage(progress * 100)
        //使用Handler每500毫秒更新一次进度条
        handler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 500)
    }


    //更新按钮的文字
    fun updatePlayText() {
        if (musicControl!!.isPlaying()) {
            startAnim()
            handler.sendEmptyMessage(UPDATE_PROGRESS)
        } else {
            stopAnim()
        }
    }

    var anim: ObjectAnimator? = null
    fun startAnim() {
        anim = ObjectAnimator.ofFloat(cover_music_im, "rotation", 0F, 360F)
        anim!!.setInterpolator(LinearInterpolator())
        anim!!.setDuration(2000)
        anim!!.setRepeatCount(-1)
        anim!!.start()
    }

    fun stopAnim() {
        anim!!.end()
    }


    //调用MyBinder中的play()方法
    fun play() {
        musicControl!!.play()
        updatePlayText()
    }


    override fun onResume() {
        super.onResume()
        //进入到界面后开始更新进度条
        if (musicControl != null) {
            handler.sendEmptyMessage(UPDATE_PROGRESS)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //退出应用后与service解除绑定
        unbindService(conn)
    }

    override fun onStop() {
        super.onStop()
        //停止更新进度条的进度
        handler.removeCallbacksAndMessages(null)
    }
}
