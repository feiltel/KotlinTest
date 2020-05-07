package com.nut2014.kotlintest.view


import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nut2014.baselibrary.anim.RotationAnim
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
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*


class InfoFragment : BaseFragment() {
    private var coverData: Cover? = null
    private lateinit var musicConnection: MusicConnection
    private var musicControl: MusicService.MusicBinder? = null
    private var isReady: Boolean = false

    private val jumpEditRequestCode = 12
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_tb.title = ""

        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar_tb)
        (requireActivity() as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        coverData = CommonConfig.fromJson(arguments?.getString("cover")!!, Cover::class.java)

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

    private fun setCancheView(view: View) {
        view.isDrawingCacheEnabled = true
        view.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        view.drawingCacheBackgroundColor = Color.WHITE
        view.buildDrawingCache()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> findNavController().navigateUp()
            R.id.edit -> {
                jumpEdit(coverData!!)
            }
            R.id.share -> allShare()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.info_menu, menu)
        menu.getItem(0).isVisible = UserDataUtils.getId() == coverData!!.user_id
    }

    private lateinit var saveFile: File
    private fun allShare() {
        setCancheView(root_coor)
        val task = GlobalScope.launch(Dispatchers.Unconfined, CoroutineStart.LAZY) {
            //调用s
            val bmp = root_coor.drawingCache // 获取图片
            saveFile = FileUtils.savePicture(bmp, FileUtils.rootPath + "/test/", "test.jpg")
            root_coor.destroyDrawingCache() // 保存过后释放资源
        }
        //调用后先执行之前方法 然后执行子线程 最后在执行后面的方法
        task.start()
        //分享
        ShareUtils.share(
            requireContext(), resources.getString(R.string.share), coverData!!.coverDes,
            "${getString(R.string.at)}${coverData!!.userName}", saveFile
        )


    }


    private fun jumpEdit(coverData: Cover) {
        val args = Bundle()
        args.putInt("cover", coverData.id)
        findNavController().navigate(R.id.action_infoFragment_to_addCoverFragment, args)

    }

    private fun initMusic() {
        anim = RotationAnim(cover_music_im)
        musicConnection = MusicConnection()
        requireContext().bindService(
            Intent(requireContext(), MusicService::class.java),
            musicConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    private inner class MusicConnection : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            musicControl = service as MusicService.MusicBinder
            if (coverData!!.coverMusicPath.isNotEmpty()) {
                musicControl!!.loadData(
                    coverData!!.coverMusicPath, MediaPlayer.OnPreparedListener {
                        it.start()
                        anim!!.start()
                        isReady = true
                    }, MediaPlayer.OnBufferingUpdateListener { _, i ->
                        progress_circular.setPercentage(i * 1F)
                    }
                )
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
        author_tv.text = "${resources.getString(R.string.at)}${coverData.userName}"
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

        ImageUtils.loadCircleImg(requireContext(), coverData.musicCoverPath, cover_music_im)
        music_name_tv.text = coverData.musicName
        music_artist_tv.text = coverData.artistName
        like_num_tv.text = coverData.likeNumber.toString()
    }


    private var anim: RotationAnim? = null

    override fun onDestroy() {
        super.onDestroy()
        anim!!.end()
        requireContext().unbindService(musicConnection)
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
        if (requestCode == jumpEditRequestCode && resultCode == 1) {
            getDataSet()
        }
    }
}
