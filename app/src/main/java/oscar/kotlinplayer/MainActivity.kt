package oscar.kotlinplayer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.os.IBinder
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onClick
import org.jetbrains.anko.onSeekBarChangeListener
import org.jetbrains.anko.support.v4.onPageChangeListener
import org.jetbrains.anko.uiThread
import oscar.kotlinplayer.adapter.CoverPagerAdapter
import oscar.kotlinplayer.event.SongEvent
import oscar.kotlinplayer.manager.SongManager
import oscar.kotlinplayer.service.PlayService
import oscar.kotlinplayer.utils.ImageUtil
import oscar.kotlinplayer.utils.getSongImg



class MainActivity : AppCompatActivity() {

    lateinit var coverPagerAdapter: CoverPagerAdapter
    lateinit var serviceConnection: ServiceConnection
    private var mPlayService: PlayService? = null
    var isTouchSeekBar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EventBus.getDefault().register(this);

        song_list.songList = SongManager.instance.songList

        coverPagerAdapter = CoverPagerAdapter(this)
        cover_view_pager.coverAdapter = coverPagerAdapter
        coverPagerAdapter.songList = SongManager.instance.songList
        coverPagerAdapter.notifyDataSetChanged()

        show_list_btn.onClick {
            if(song_list.visibility == View.GONE){
                song_list.visibility = View.VISIBLE
            }else{
                song_list.visibility = View.GONE
            }
        }

        //SongManager.instance.setCurSong(0, SongEvent.Event.START)
        var song = SongManager.instance.curSong()
        song_txt.text = song.title
        singer_txt.text = song.artist
        coverPagerAdapter.coverViewList[0].start()

        previous_btn.onClick { SongManager.instance.preSong(SongEvent.Event.START) }
        next_btn.onClick { SongManager.instance.nextSong(SongEvent.Event.START) }
        play_btn.onClick{
            if(mPlayService!!.isPlaying()){
                var index = SongManager.instance.curIndex
                coverPagerAdapter.coverViewList[index].stop()

                mPlayService!!.pause()
                play_btn.setBackgroundResource(R.drawable.btn_play)
            }else{
                var index = SongManager.instance.curIndex
                coverPagerAdapter.coverViewList[index].start()

                mPlayService!!.start()
                play_seek_bar.setMax(mPlayService!!.getDuration())
                total_time.text = formatTime(mPlayService!!.getDuration())
                play_btn.setBackgroundResource(R.drawable.btn_pause)
            }
        }

        cover_view_pager.onPageChangeListener {
            onPageSelected {position ->
                SongManager.instance.setCurSong(position, SongEvent.Event.START)
            }
            onPageScrollStateChanged {
                state->when(state){
                    ViewPager.SCROLL_STATE_IDLE->{
                        var index = SongManager.instance.curIndex
                        coverPagerAdapter.coverViewList[index].start()
                    }
                    ViewPager.SCROLL_STATE_DRAGGING,ViewPager.SCROLL_STATE_SETTLING->{
                        var index = SongManager.instance.curIndex
                        coverPagerAdapter.coverViewList[index].stop()
                    }
                }
            }
        }

        play_seek_bar.onSeekBarChangeListener {

            onProgressChanged{seekBar, progress, fromUser -> if(fromUser) {current_time.text = formatTime(seekBar!!.progress)}}

            onStartTrackingTouch {
                seekBar -> run{
                    isTouchSeekBar = true
                }
            }

            onStopTrackingTouch {
                seekBar -> run{
                    if(mPlayService != null)
                        mPlayService!!.seekTo(seekBar!!.progress)
                    isTouchSeekBar = false
                }
            }

        }

        serviceConnection = object: ServiceConnection{
            override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
                Log.d("onServiceConnected", "connected")
                if (binder == null) {
                    return
                }
                mPlayService = (binder as PlayService.PlayBinder).service
                SongManager.instance.setCurSong(0, SongEvent.Event.START)
/*                var song = SongManager.instance.curSong()
                mPlayService!!.setSong(song)
                mPlayService!!.start()
                play_seek_bar.setMax(mPlayService!!.getDuration())
                total_time.text = formatTime(mPlayService!!.getDuration())
                play_btn.setBackgroundResource(R.drawable.btn_pause)*/
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
        val intent = Intent(this, PlayService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSongEvent(songEvent: SongEvent){
        when(songEvent.event){
            SongEvent.Event.START -> {
                var song = songEvent.song
                if(cover_view_pager.currentItem != SongManager.instance.curIndex) {
                    cover_view_pager.currentItem = SongManager.instance.curIndex
                }
                song_txt.text = song.title
                singer_txt.text = song.artist
                song_list.songListAdapter!!.notifyDataSetChanged()
                if(mPlayService != null){
                    mPlayService!!.setSong(song)
                    mPlayService!!.start()
                    play_seek_bar.setMax(mPlayService!!.getDuration())
                    total_time.text = formatTime(mPlayService!!.getDuration())
                    play_btn.setBackgroundResource(R.drawable.btn_pause)
                }

                //var bgBitmap = getSongImg(song, applicationContext)
                //var curBitmap = ImageUtil.doCut(bitmap, width, height)
                //var blurBitmap = ImageUtil.doBlur(bgBitmap, 20)
                //background_view.setBlurBitmap(bgBitmap)
                //background_view.setBitmap(bgBitmap)



                doAsync {
                    var bgBitmap = getSongImg(song, applicationContext)
                    var cutBitmap = ImageUtil.doCut(bgBitmap, background_view.width, background_view.height)
                    var blurBitmap = ImageUtil.doBlur(cutBitmap, 20)
                    var matrix = Matrix()
                    matrix.postScale(background_view.width.toFloat() / blurBitmap.width, background_view.height.toFloat() / blurBitmap.height )
                    Thread.sleep(500)

                    val resizeBmp = Bitmap.createBitmap(blurBitmap, 0, 0, blurBitmap.width,
                            blurBitmap.height, matrix, true)
                    uiThread {
                        background_view.setBitmap(resizeBmp)
                    }
                }
            }
            SongEvent.Event.PROGRESS -> {
                if(isTouchSeekBar) {
                    return;
                }
                play_seek_bar.progress = songEvent.value
                current_time.text = formatTime(songEvent.value)
            }
            SongEvent.Event.COMPLETE -> {
                SongManager.instance.nextSong(SongEvent.Event.START)
            }
            else -> {

            }
        }
    }

    fun formatTime(progress: Int): String{
        var secondCount = progress / 1000
        var second = secondCount % 60
        var minute = secondCount / 60
        return String.format("%02d:%02d", minute, second)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }
}
