package com.nut2014.kotlintest.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import com.jaeger.library.StatusBarUtil
import com.nut2014.baselibrary.anim.RotationAnim
import com.nut2014.baselibrary.base.BaseActivity
import com.nut2014.baselibrary.transformer.ZoomOutTransformer
import com.nut2014.baselibrary.uitls.FileUtils
import com.nut2014.baselibrary.uitls.ImageUtils
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.base.CommonConfig
import com.nut2014.kotlintest.base.MyApplication
import com.nut2014.kotlintest.entity.Cover
import com.nut2014.kotlintest.network.runRxLambda
import com.nut2014.kotlintest.service.MusicService
import com.nut2014.kotlintest.utils.GlideImageLoader
import com.nut2014.kotlintest.utils.ShareUtils
import com.nut2014.kotlintest.utils.UserDataUtils
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast
import java.io.File
import java.util.*


class InfoActivity : BaseActivity() {
    private var coverData: Cover? = null
    private lateinit var musicConnection:MusicConnection
    private var musicControl: MusicService.MusicBinder? = null
    private var isReady: Boolean = false

    private val INFO_JUMP_EDIT_REQUEST_CODE = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setTransparent(this)
        setContentView(R.layout.activity_info)
        toolbar_tb.title = ""
        setSupportActionBar(toolbar_tb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        coverData = CommonConfig.fromJson(intent.getStringExtra("cover")!!, Cover::class.java)

        getDataSet()
        initMusic()
        initView(coverData!!)

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

    private lateinit var saveFile: File
    private fun allShare() {
        root_coor.isDrawingCacheEnabled = true
        root_coor.buildDrawingCache()
        val task = GlobalScope.launch(Unconfined, CoroutineStart.LAZY) {
            //调用s
            val bmp = root_coor.getDrawingCache() // 获取图片
            saveFile = FileUtils.savePicture(bmp, FileUtils.rootPath + "/test/", "test.jpg")
            root_coor.destroyDrawingCache() // 保存过后释放资源
        }
        //调用后先执行之前方法 然后执行子线程 最后在执行后面的方法
        task.start()
        //分享
        ShareUtils.share(
            this, resources.getString(R.string.share), coverData!!.coverDes,
            "${getString(R.string.at)}${coverData!!.userName}", saveFile
        )


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
            val intent = Intent(this@InfoActivity, AddCoverActivity::class.java)
            intent.putExtra("cover", coverData!!.id)
            startActivityForResult(intent, INFO_JUMP_EDIT_REQUEST_CODE)
        } else if (itemId == R.id.share) {
            toast(item.title)
            allShare()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initMusic() {

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
            if (coverData!!.coverMusicPath.isNotEmpty()) {
                musicControl!!.loadData(
                    coverData!!.coverMusicPath,
                    {
                        //准备完成
                        it.start()
                        anim!!.start()
                        isReady = true
                    },
                    { _, percent ->
                        //缓冲进度
                        progress_circular.setPercentage(percent * 1F)
                    })
            }


        }

        override fun onServiceDisconnected(name: ComponentName) {
            anim!!.end()
            musicControl = null
        }
    }

    private fun initView(coverData: Cover) {
        content_tv.text = coverData.coverDes
        like_num_tv.text = coverData.likeNumber.toString()
        author_tv.text = resources.getString(R.string.at) + coverData.userName
        initBanner(coverData.coverImgPath)
        setMusicInfo(coverData)
    }

    private fun getDataSet() {

        runRxLambda(MyApplication.application().getService().getCoverInfo(coverData!!.id), {
            println(it.data.likeCover)
            if (it.code == 1) {
                val data = it.data
                coverData = data
                initView(data)

                if (data.likeCover > 0) {
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
    private fun initBanner(coverImgPath: String) {
        list_rv.setImageLoader(GlideImageLoader())
        val paths = ArrayList<String>()
        val split = coverImgPath.split(",")
        split.forEach {
            if (it.isNotEmpty()) {
                paths.add(it)
            }
        }
        list_rv!!.setImages(paths)
        list_rv!!.setDelayTime(3000)
        list_rv!!.setBannerStyle(BannerConfig.NOT_INDICATOR)
        list_rv!!.setPageTransformer(true, ZoomOutTransformer())
        list_rv.start()
    }


    /**
     * 设置封面
     * 设置名称
     * 设置歌手
     */
    private fun setMusicInfo(coverData: Cover) {

        ImageUtils.loadCircleImg(this, coverData.musicCoverPath, cover_music_im)
        music_name_tv.text = coverData.musicName
        music_artist_tv.text = coverData.artistName
        like_num_tv.text = coverData.likeNumber.toString()
    }


    private var anim: RotationAnim? = null


    override fun onDestroy() {
        super.onDestroy()
        anim!!.end()
        unbindService(musicConnection)
    }

    override fun onStop() {
        super.onStop()
        musicControl!!.pause()
    }

    override fun onResume() {
        super.onResume()
        if (isReady) {
            musicControl!!.start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == INFO_JUMP_EDIT_REQUEST_CODE && resultCode == 1) {
            getDataSet()
        }
    }
}
