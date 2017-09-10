package oscar.kotlinplayer

import android.app.Application
import oscar.kotlinplayer.utils.PermissionsChecker

/**
 * Created by oscar on 2017/7/29.
 */

class MyApplication : Application(){

    companion object {
        @JvmField
        var instance : MyApplication ? = null;

        @JvmStatic
        fun getInstance() : MyApplication ? {
            return instance;
        }
    }


    override fun onCreate() {
        super.onCreate()
        instance = this;
    }
}