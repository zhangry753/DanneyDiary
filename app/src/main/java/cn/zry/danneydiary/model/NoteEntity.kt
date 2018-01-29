package cn.zry.danneydiary.model

import android.arch.persistence.room.*
import cn.zry.danneydiary.room.RoomConverters
import java.util.*

/**
 * Created by zry on 2018/1/28.
 */
@Entity(tableName = "Note")
@TypeConverters(RoomConverters::class)
data class NoteEntity(
        @PrimaryKey @ColumnInfo(name = "id") var id: Long
) {
    @Ignore constructor():this(0)

    @ColumnInfo(name = "date")
    var date: Date = Date()

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "content")
    var content: String = ""

    @ColumnInfo(name = "imageId")
    var imageId: Int = 0

}