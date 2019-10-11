package com.nut2014.kotlintest.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.jaeger.library.StatusBarUtil
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.base.BaseActivity
import com.nut2014.kotlintest.base.CommonConfig
import com.nut2014.kotlintest.base.MyApplication
import com.nut2014.kotlintest.entity.Cover
import com.nut2014.kotlintest.network.runRxLambda
import com.nut2014.kotlintest.service.MusicService
import com.nut2014.kotlintest.utils.GlideImageLoader
import com.nut2014.kotlintest.utils.RotationAnim
import com.nut2014.kotlintest.utils.UserDataUtils
import kotlinx.android.synthetic.main.activity_info.*
import org.jetbrains.anko.toast
import java.util.*


class InfoActivity : BaseActivity() {
    private var coverData: Cover? = null
    private var musicConnection: MusicConnection? = null
    private var musicControl: MusicService.MusicBinder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setTransparent(this)
        setContentView(R.layout.activity_info)
        toolbar_tb.title = ""
        setSupportActionBar(toolbar_tb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        getDataSet()
        initBanner()
        initMusic()

        cover_music_fl.setOnClickListener {
            musicControl!!.changeStatue()
            if (musicControl!!.isPlaying) {
                anim!!.animator.resume()
            } else {
                anim!!.animator.pause()
            }
        }
        like_im.setOnClickListener {
            likeAct()
        }


    }

    private fun likeAct() {
        runRxLambda(MyApplication.application().getService().likeCover(coverData!!.id), {
            println(it.data.likeCover)
            if (it.code == 1) {
                like_num_tv.text = it.data.likeNumber.toString()
                if (it.data.likeCover > 0) {
                    like_im.setImageResource(R.drawable.ic_favorite_8dp)
                } else {
                    like_im.setImageResource(R.drawable.ic_favorite_border_8dp)
                }
            }
        }, {

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.info_menu, menu)
        menu!!.getItem(0).isVisible = UserDataUtils.getId() == coverData!!.user_id
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val itemId = item!!.itemId
        if (itemId == android.R.id.home) {
            onBackPressed()
        } else if (itemId == R.id.edit) {
            finish()
            val intent = Intent(this@InfoActivity, AddCoverActivity::class.java)
            intent.putExtra("cover", coverData!!.id)
            startActivity(intent)
        }else if (itemId == R.id.share) {
           toast(item.title)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initMusic() {
        setMusicInfo()
        anim = RotationAnim(cover_music_im)
        musicConnection = MusicConnection()
        bindService(
            Intent(this, MusicService::class.java),
            musicConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    private inner class MusicConnection : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            musicControl = service as MusicService.MusicBinder
            musicControl!!.loadData(
                coverData!!.coverMusicPath,
                {
                    //准备完成
                    it.start()
                    anim!!.start()
                },
                { mp, percent ->
                    //缓冲进度
                    progress_circular.setPercentage(percent * 1F)
                })

        }

        override fun onServiceDisconnected(name: ComponentName) {
            anim!!.end()
            musicControl = null
        }
    }


    private fun getDataSet() {
        coverData = CommonConfig.fromJson(intent.getStringExtra("cover"), Cover::class.java)
        content_tv.text = coverData!!.coverDes

        runRxLambda(MyApplication.application().getService().getCoverInfo(coverData!!.id), {
            println(it.data.likeCover)
            if (it.code == 1) {
                like_num_tv.text = it.data.likeNumber.toString()
                author_tv.text= "${getString(R.string.at)}${it.data.userName}"
                if (it.data.likeCover > 0) {
                    like_im.setImageResource(R.drawable.ic_favorite_8dp)
                } else {
                    like_im.setImageResource(R.drawable.ic_favorite_border_8dp)
                }
            }
        }, {

        })
    }

    /**
     * 初始化
     */
    private fun initBanner() {
        list_rv.setImageLoader(GlideImageLoader())
        val paths = ArrayList<String>()
        val split = coverData!!.coverImgPath.split(",")
        split.forEach {
            paths.add(it)
        }
        list_rv!!.setImages(paths)
        list_rv.start()
    }


    /**
     * 设置封面
     * 设置名称
     * 设置歌手
     */
    private fun setMusicInfo() {
        Glide.with(this).load(coverData!!.musicCoverPath).into(cover_music_im)
        music_name_tv.text = coverData!!.musicName
        music_artist_tv.text = coverData!!.artistName
        like_num_tv.text = coverData!!.likeNumber.toString()
    }


    private var anim: RotationAnim? = null


    override fun onDestroy() {
        super.onDestroy()
        anim!!.end()
        unbindService(musicConnection)
    }

    override fun onStop() {
        super.onStop()
    }
}
