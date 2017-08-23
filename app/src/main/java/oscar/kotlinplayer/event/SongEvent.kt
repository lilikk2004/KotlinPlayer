package oscar.kotlinplayer.event

import oscar.kotlinplayer.bean.Song

class SongEvent(var song: Song, var event: Event){
    enum class Event{ START, STOP, PAUSE, PROGRESS }

    var value: Int = 0
}