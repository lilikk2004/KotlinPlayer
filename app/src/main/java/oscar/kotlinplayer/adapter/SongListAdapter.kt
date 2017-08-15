package oscar.kotlinplayer.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.song_list_item.view.*
import org.jetbrains.anko.textColor
import oscar.kotlinplayer.R
import oscar.kotlinplayer.bean.Song
import oscar.kotlinplayer.manager.SongManager

/**
 * Created by oscar on 2015/11/20.
 */
class SongListAdapter(var context: Context) : BaseAdapter() {

    var songList: MutableList<Song> = mutableListOf()

    inner class ViewHolder {
        var songTxt: TextView? = null
        var singerTxt: TextView? = null
    }

    override fun getCount(): Int {
        return songList.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        Log.e("SongListAdapter", "getView()");
        var convertView2 = convertView
        var viewHolder: ViewHolder? = null
        if (convertView2 == null) {
            convertView2 = LayoutInflater.from(context).inflate(
                    R.layout.song_list_item, null)

            viewHolder = ViewHolder()
            viewHolder.songTxt = convertView2.song_name
            viewHolder.singerTxt = convertView2.singer_name
            convertView2.tag = viewHolder
        } else {
            viewHolder = convertView2.tag as ViewHolder
        }

        val song = songList[position]

        viewHolder.songTxt!!.text = song.title
        viewHolder.singerTxt!!.text = song.artist

        if (position == SongManager.instance.curIndex) {
            viewHolder.songTxt!!.textColor = context.resources.getColor(R.color.colorSongSelect)
            viewHolder.singerTxt!!.textColor = context.resources.getColor(R.color.colorSingerSelect)
        } else {
            viewHolder.songTxt!!.textColor = context.resources.getColor(R.color.colorSongNormal)
            viewHolder.singerTxt!!.textColor = context.resources.getColor(R.color.colorSingerNormal)
        }

        return convertView2
    }
}
