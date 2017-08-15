package oscar.kotlinplayer.bean

/**
 * Created by oscar on 2017/7/29.
 */

class Song(var filePath: String,
           var title: String,
           var artist: String,
           var albumId: Int){
    constructor(): this("", "", "", 0)
}