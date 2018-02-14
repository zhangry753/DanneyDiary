package cn.zry.danneydiary

import android.app.Application
import android.arch.persistence.room.Room
import cn.zry.danneydiary.room.AppDatabase
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager



/**
 * Created by zry on 2018/1/28.
 */
class MyApplication : Application() {
    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(this, AppDatabase::class.java,
                "danneyDiary")
                .fallbackToDestructiveMigration()
                .build()
    }
}