package oscar.kotlinplayer.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import oscar.kotlinplayer.bean.Song

/**
 * Created by oscar on 2017/8/9.
 */
class PlayService() : Service() {

    var mediaPlayer: MediaPlayer = MediaPlayer()
    var playBinder = PlayBinder()

    inner class PlayBinder : Binder() {
        val service: PlayService
            get() = this@PlayService
    }

    override fun onBind(p0: Intent?): IBinder {
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
}