package oscar.kotlinplayer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.onClick
import oscar.kotlinplayer.adapter.CoverPagerAdapter
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

        previous_btn.onClick { SongManager.instance.preSong() }
        next_btn.onClick { SongManager.instance.nextSong() }
    }
}
