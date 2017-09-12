package oscar.kotlinplayer.control

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.song_list_layout.view.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.onItemClick
import oscar.kotlinplayer.R
import oscar.kotlinplayer.adapter.SongListAdapter
import oscar.kotlinplayer.bean.Song
import oscar.kotlinplayer.event.SongEvent
import oscar.kotlinplayer.manager.SongManager

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
        //list_bottom_layout.onClick { visibility = GONE }
        list_bottom_layout.onClick {
            val animation = AnimationUtils.loadAnimation(context, R.anim.abc_slide_out_bottom)
            startAnimation(animation)
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                    visibility = View.GONE
                }

                override fun onAnimationStart(p0: Animation?) {
                }

            })
        }
        play_list.onItemClick { adapterView, view, i, l -> SongManager.instance.setCurSong(i, SongEvent.Event.START)  }
    }
}