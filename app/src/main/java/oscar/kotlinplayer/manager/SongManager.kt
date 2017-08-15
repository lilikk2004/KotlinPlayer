package oscar.kotlinplayer.manager

import android.database.Cursor
import android.provider.MediaStore
import oscar.kotlinplayer.MyApplication
import oscar.kotlinplayer.bean.Song

/**
 * Created by oscar on 2017/7/29.
 */
class SongManager private constructor(){
    private object Holder{val mInstance = SongManager()}
    companion object {
        @JvmField var TAG = "SongManager"

        val instance:SongManager by lazy { Holder.mInstance }
    }

    var songList: MutableList<Song> = mutableListOf()
    var curIndex: Int = 0

    fun init(){

        songList.clear()

        val c: Cursor = MyApplication.instance!!.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER)

        if (!c.moveToFirst()) {
            return
        }

        do {
            // 通过Cursor 获取路径，如果路径相同则break；
            var filePath = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
            // 查找到相同的路径则返回，此时cursorPosition 便是指向路径所指向的Cursor 便可以返回了

            if(!filePath.endsWith(".mp3")){
                continue
            }

            var title = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))

            var artist = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))

            var albumId =c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))

            var song: Song = Song(filePath, title, artist, albumId);
            if(filePath.endsWith("mp3")) {
                songList.add(song)
            }
        } while (c.moveToNext())
    }

    fun nextSong(){
        if(curIndex < songList.size - 1){
            curIndex++
        }
    }

    fun preSong(){
        if(curIndex > 0){
            curIndex--
        }
    }
}