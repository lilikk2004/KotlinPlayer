package oscar.kotlinplayer

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.AlertDialog.THEME_HOLO_LIGHT
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.view.View
import android.view.Window
import android.view.WindowManager
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import oscar.kotlinplayer.manager.SongManager
import oscar.kotlinplayer.utils.PermissionsChecker











class WelcomeActivity : Activity() {

    var permissionsChecker = PermissionsChecker(this)
    var bRequireCheck = true // 是否需要系统权限检测, 防止和系统提示框重叠
    var PERMISSION_REQUEST_CODE = 233;//权限请求回调

    val PACKAGE_URL_SCHEME = "package:"

    //val permissions = ArrayList<String>()
    //vararg permissions:String = {Manifest.permission.WRITE_EXTERNAL_STORAGE}
    val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome)

        // 隐藏状态栏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏底部虚拟按键
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

    override fun onResume() {
        super.onResume()
        //初始化工作(获取系统权限)
        if (bRequireCheck) {
            bRequireCheck = false
            if (permissionsChecker.lacksPermissions(permissions)) {//判断是否有所需要的权限
                requestPermissions(permissions) // 请求权限
            } else {
                // 全部权限都已获取
                initSong()
            }
        }
    }

    // 请求权限兼容低版本
    private fun requestPermissions(permissions:Array<String>) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults.size == 0) {
            return
        }
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            initSong()
        } else {
            showMissingPermissionDialog()
        }
    }

    // 显示缺失权限提示
    private fun showMissingPermissionDialog() {
        val builder = AlertDialog.Builder(this, THEME_HOLO_LIGHT)
        builder.setTitle(R.string.help)
        builder.setMessage(R.string.permission_request_tip)

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.quit, DialogInterface.OnClickListener { dialog, which -> finish() })

        builder.setPositiveButton(R.string.action_settings, DialogInterface.OnClickListener { dialog, which ->
            bRequireCheck = true
            startAppSettings()
        })

        builder.setCancelable(false)

        builder.show()
    }


    // 启动应用的设置
    private fun startAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse(PACKAGE_URL_SCHEME + packageName)
        startActivity(intent)
    }


    // 判断是否所有权限都通过
    private fun hasAllPermissionsGranted(grantResults: IntArray): Boolean {
        return !grantResults.contains(PackageManager.PERMISSION_DENIED)
    }

    fun initSong(){
        doAsync {
            SongManager.instance.init()
            uiThread {
                var intent = Intent(this@WelcomeActivity, MainActivity::class.java);
                startActivity(intent);
            }
        }
    }
}
