package oscar.kotlinplayer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.onClick
import oscar.kotlinplayer.adapter.CoverPagerAdapter
import oscar.kotlinplayer.event.SongEvent
import oscar.kotlinplayer.manager.SongManager

class MainActivity : AppCompatActivity() {

    lateinit var coverPagerAdapter: CoverPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
    }
}
