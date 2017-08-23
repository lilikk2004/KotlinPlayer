package oscar.kotlinplayer.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import org.greenrobot.eventbus.EventBus
import oscar.kotlinplayer.bean.Song
import oscar.kotlinplayer.event.SongEvent
import oscar.kotlinplayer.manager.SongManager
import java.util.*

/**
 * Created by oscar on 2017/8/9.
 */
class PlayService() : Service() {

    var mediaPlayer: MediaPlayer = MediaPlayer()
    var playBinder = PlayBinder()
    var timer = Timer()
    var timerTask = object: TimerTask(){
        override fun run() {
            if(mediaPlayer.isPlaying) {
                var curSong = SongManager.instance.curSong()
                var event = SongEvent(curSong, SongEvent.Event.PROGRESS)
                event.value = mediaPlayer.currentPosition
                EventBus.getDefault().post(event)
            }
        }
    }

    inner class PlayBinder : Binder() {
        val service: PlayService
            get() = this@PlayService
    }

    override fun onBind(p0: Intent?): IBinder {
        timer.schedule(timerTask, 0, 25)
        mediaPlayer.setOnCompletionListener {
            var curSong = SongManager.instance.curSong()
            var event = SongEvent(curSong, SongEvent.Event.COMPLETE)
            EventBus.getDefault().post(event)
        }
        return playBinder
    }

     fun setSong(song: Song){
         try {
             mediaPlayer.stop()
             mediaPlayer.reset()
             mediaPlayer.setDataSource(song.filePath)
             mediaPlayer.prepare()
         }catch (e: Exception){
             e.printStackTrace()
         }
     }

    fun start(){
        try {
            mediaPlayer.start()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }


    fun pause(){
        try {
            mediaPlayer.pause()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun seekTo(msec: Int){
        try {
            mediaPlayer.seekTo(msec)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun getDuration(): Int{
        return mediaPlayer.duration
    }

    fun isPlaying(): Boolean{
        return mediaPlayer.isPlaying
    }


}