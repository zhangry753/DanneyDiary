package cn.zry.danneydiary.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import cn.zry.danneydiary.dao.NoteDao
import cn.zry.danneydiary.model.NoteEntity

/**
 * Created by zry on 2018/1/28.
 */
@Database(entities = arrayOf(NoteEntity::class), version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
}