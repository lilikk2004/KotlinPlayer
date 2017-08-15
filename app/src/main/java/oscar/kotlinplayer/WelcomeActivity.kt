package oscar.kotlinplayer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import oscar.kotlinplayer.manager.SongManager

class WelcomeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome)

/*        thread(start = true) {
            SongManager.instance.init()
        }*/
        // 隐藏状态栏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏底部虚拟按键
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        doAsync {
            SongManager.instance.init()
            uiThread {
                var intent = Intent(this@WelcomeActivity, MainActivity::class.java);
                startActivity(intent);
            }
        }
    }
}
