package oscar.kotlinplayer.control

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.song_list_layout.view.*
import org.jetbrains.anko.onClick
import oscar.kotlinplayer.R
import oscar.kotlinplayer.adapter.SongListAdapter
import oscar.kotlinplayer.bean.Song

/**
 * Created by oscar on 2017/8/6.
 */
public class SongListLayout : RelativeLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs : AttributeSet) : super(context, attrs)

    var songListAdapter: SongListAdapter? = null
    var songList: MutableList<Song> = mutableListOf()
    set(value) {
        songListAdapter!!.songList = value
        songListAdapter!!.notifyDataSetChanged()

    }

    init {
        LayoutInflater.from(context).inflate(R.layout.song_list_layout, this)
        songListAdapter = SongListAdapter(context)
        play_list.adapter = songListAdapter
        list_bottom_layout.onClick { visibility = GONE }
    }
}