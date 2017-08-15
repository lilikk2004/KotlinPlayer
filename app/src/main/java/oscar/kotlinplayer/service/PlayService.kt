package oscar.kotlinplayer.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import oscar.kotlinplayer.bean.Song

/**
 * Created by oscar on 2017/8/9.
 */
class PlayService() : Service() {

    lateinit var mediaPlayer: MediaPlayer

    override fun onBind(p0: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        mediaPlayer = MediaPlayer()
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