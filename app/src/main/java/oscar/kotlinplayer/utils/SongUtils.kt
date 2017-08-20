package oscar.kotlinplayer.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import oscar.kotlinplayer.bean.Song

/**
 * Created by oscar on 2017/8/20.
 */


/**

 * 功能 通过album_id查找 album_art 如果找不到返回null

 * @param albumId
 *  封面的id
 * @return album_art
 */
fun getAlbumArt(albumId: Int, context: Context): String {
    val mUriAlbums = "content://media/external/audio/albums"
    val projection = arrayOf("album_art")
    var cur = context.contentResolver.query(
            Uri.parse(mUriAlbums + "/" + Integer.toString(albumId)),
            projection, null, null, null)
    var album_art: String = ""
    if (cur.count > 0 && cur.columnCount > 0) {
        cur.moveToNext()
        album_art = cur.getString(0)
    }
    cur.close()
    return album_art
}

fun getSongImg(song: Song,context: Context): Bitmap{
    val albumArt = getAlbumArt(song.albumId, context)
    return BitmapFactory.decodeFile(albumArt)
}