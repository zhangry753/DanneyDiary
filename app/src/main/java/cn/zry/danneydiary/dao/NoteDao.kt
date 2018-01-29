package cn.zry.danneydiary.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import cn.zry.danneydiary.model.NoteEntity

/**
 * Created by zry on 2018/1/28.
 */
@Dao
interface NoteDao {
    /**
     * 向表中插入一系列数据,覆盖
    */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg user: NoteEntity):List<Long>

    @Insert
    fun insert(users: List<NoteEntity>):List<Long>

    @Query("SELECT * FROM note WHERE id = :arg0 ")
    fun getById(id: Long): NoteEntity

    @Query("SELECT * FROM note")
    fun getAll(): List<NoteEntity>

}