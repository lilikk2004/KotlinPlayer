package oscar.kotlinplayer.utils

import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

/**
 * 检查权限的工具类
 *
 *
 * Created by wangchenlong on 16/1/26.
 */
class PermissionsChecker(val context: Context) {

    // 判断权限集合
    fun lacksPermissions(permissions:Array<String>): Boolean {
        return permissions.any { lacksPermission(it) }
    }

    // 判断是否缺少权限
    private fun lacksPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context.applicationContext, permission) == PackageManager.PERMISSION_DENIED
    }
}
