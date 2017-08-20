package oscar.kotlinplayer

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.onClick
import org.jetbrains.anko.support.v4.onPageChangeListener
import oscar.kotlinplayer.adapter.CoverPagerAdapter
import oscar.kotlinplayer.event.SongEvent
import oscar.kotlinplayer.manager.SongManager

class MainActivity : AppCompatActivity() {

    lateinit var coverPagerAdapter: CoverPagerAdapter

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

        var song = SongManager.instance.curSong()
        song_txt.text = song.title
        singer_txt.text = song.artist

        previous_btn.onClick { SongManager.instance.preSong(SongEvent.Event.START) }
        next_btn.onClick { SongManager.instance.nextSong(SongEvent.Event.START) }

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
    }

    @Subscribe
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


/*

                doAsync {
                    var bgBitmap = getSongImg(song, applicationContext)
                    uiThread {
                        background_view.setBitmap(bgBitmap)
                    }
                }*/
            }
            else -> {

            }
        }
    }
}
