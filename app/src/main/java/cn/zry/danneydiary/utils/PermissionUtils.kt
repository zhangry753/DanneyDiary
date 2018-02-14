package cn.zry.danneydiary.utils

import android.app.Activity
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.widget.Toast


/**
 * Created by zry on 2018/2/14.
 */
object PermissionUtils{

    fun externalStorage(context:Activity):Boolean{
        val PERMISSIONS_STORAGE = arrayOf(
          "android.permission.READ_EXTERNAL_STORAGE",
          "android.permission.WRITE_EXTERNAL_STORAGE"
        )
        //检测是否有写的权限
        val permission = ActivityCompat.checkSelfPermission(context,
          "android.permission.WRITE_EXTERNAL_STORAGE")
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context,"请允许访问外置存储卡",Toast.LENGTH_SHORT)
            ActivityCompat.requestPermissions(context, PERMISSIONS_STORAGE, 0)
            return false
        }
        return true
    }

}