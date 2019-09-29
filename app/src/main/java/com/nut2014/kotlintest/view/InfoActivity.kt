package com.nut2014.kotlintest.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import com.jaeger.library.StatusBarUtil
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.base.BaseActivity
import com.nut2014.kotlintest.base.CommonConfig
import com.nut2014.kotlintest.entity.Cover
import com.nut2014.kotlintest.service.PlayerServices
import com.nut2014.kotlintest.utils.AnimatorUtils
import com.nut2014.kotlintest.utils.DownloadFile
import com.nut2014.kotlintest.utils.GlideImageLoader
import com.nut2014.kotlintest.utils.MusicUtils
import kotlinx.android.synthetic.main.activity_info.*
import java.util.*


class InfoActivity : BaseActivity() {
    private var coverData: Cover? = null

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
            changePlay()
        }
    }

    private fun initMusic() {
        conn = MyConnection()
        //启动
        bindService(Intent(this, PlayerServices::class.java), conn, Context.BIND_AUTO_CREATE)
        downloadMusic(coverData!!.coverMusicPath)

    }

    private fun downloadMusic(fileUrl: String) {
        DownloadFile(this, object : DownloadFile.DownloadCallBack {
            override fun success(path: String) {
                if (isActive){
                    setMusicInfo(path)
                    musicControl!!.prepare(path)
                    musicControl!!.play()
                    updatePlayText()
                }


            }

            override fun error(msg: String) {

            }
        }).setShowProgress(false).execute(fileUrl)
    }

    private fun getDataSet() {
        coverData = CommonConfig.fromJson(intent.getStringExtra("cover"), Cover::class.java)
        content_tv.text = coverData!!.coverDes
    }

    /**
     * 初始化
     */
    private fun initBanner() {
        list_rv.setImageLoader(GlideImageLoader())
        val paths = ArrayList<String>()
        paths.add(coverData!!.coverImgPath)
        paths.add(coverData!!.coverImgPath)
        paths.add(coverData!!.coverImgPath)
        list_rv!!.setImages(paths)
        list_rv.start()
    }

    private inner class MyConnection : ServiceConnection {

        //服务启动完成后会进入到这个方法
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            //获得service中的MyBinder
            musicControl = service as PlayerServices.MyBinder

        }

        override fun onServiceDisconnected(name: ComponentName) {

        }
    }

    /**
     * 设置封面
     * 设置名称
     * 设置歌手
     */
    private fun setMusicInfo(path: String) {
        cover_music_im.setImageBitmap(MusicUtils.getCoverPicture(path))
        music_name_tv.text = MusicUtils.getCoverTitle(path)
        music_artist_tv.text = MusicUtils.getCoverArtist(path)
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
        val progress = musicControl!!.currentPosition * 1.0f / musicControl!!.duration * 1.0f
        progress_circular.setPercentage(progress * 100)
        //使用Handler每500毫秒更新一次进度条
        handler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 500)
    }


    //更新按钮的文字
    fun updatePlayText() {
        if (musicControl!!.isPlaying) {
            startAnim()
            handler.sendEmptyMessage(UPDATE_PROGRESS)
        } else {
            stopAnim()
        }
    }

    private var anim: AnimatorUtils? = null
    private fun startAnim() {
        anim = AnimatorUtils()
        anim!!.startRotation(cover_music_im)
    }

    private fun stopAnim() {
        anim!!.stopRotation()
    }


    //调用MyBinder中的play()方法
    private fun changePlay() {
        if (musicControl!!.isPlaying) {
            musicControl!!.pause()
        } else {
            musicControl!!.play()
        }
        updatePlayText()
    }


    override fun onDestroy() {
        super.onDestroy()
        //退出应用后与service解除绑定
        if (musicControl!!.isPlaying) {
            musicControl!!.stop()
        }
        //解绑service
        unbindService(conn)
        //停止service
        stopService(Intent(this, PlayerServices::class.java))
    }

    override fun onStop() {
        super.onStop()
        //停止更新进度条的进度
        handler.removeCallbacksAndMessages(null)
    }
}
