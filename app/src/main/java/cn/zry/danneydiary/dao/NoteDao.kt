package cn.zry.danneydiary.dao

import android.arch.persistence.room.*
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
    fun insertOrUpdate(vararg user: NoteEntity): List<Long>

    @Insert
    fun insert(users: List<NoteEntity>): List<Long>

    @Query("SELECT * FROM note WHERE id = :arg0 ")
    fun getById(id: Long): NoteEntity

    @Query("SELECT * FROM note")
    fun getAll(): List<NoteEntity>

    @Delete
    fun delete(vararg note:NoteEntity)

    /**
     * 通过日期查询，日期格式"yyyy年MM月dd日"
     */
    @Query("SELECT * FROM note WHERE date Like :arg0||'%' ")
    fun getByDay(dayStr: String): List<NoteEntity>

//    @Query("SELECT * FROM note WHERE date Like ':arg0%' GROUP BY ")
//    fun getCountByMonth(monthStr:String): List<NoteEntity>

}