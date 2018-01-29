package cn.zry.danneydiary.activity

import android.arch.core.internal.SafeIterableMap
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import cn.zry.danneydiary.MyApplication
import cn.zry.danneydiary.widget.CustomCalendar
import cn.zry.danneydiary.R
import cn.zry.danneydiary.dao.NoteDao
import cn.zry.danneydiary.model.NoteEntity
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_main.*
import kotterknife.bindView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private val noteRecyclerView: ListView by bindView(R.id.noteRecyclerView)
    private val calendar: CustomCalendar by bindView(R.id.calendar)

    private lateinit var noteDao: NoteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", { view ->
                        Toast.makeText(this, "点击了", Toast.LENGTH_SHORT).show()
                    }).show()
        }

        noteDao = (application as MyApplication).db.noteDao()

//        val note = NoteEntity()
//        note.title = "title"
//        Observable.create(ObservableOnSubscribe<Long> { e ->
//            val id = noteDao.insert(note)
//            if (id.size > 0)
//                e.onNext(id[0])
//        }).observeOn(Schedulers.newThread())
//                .subscribeOn(Schedulers.newThread())
//                .subscribe({ value ->
//                    System.out.println(value)
//                })

    }

    override fun onResume() {
        super.onResume()
        // 加载note
        val noteList = ArrayList<Map<String, Any>>()
        for (i in 0..10) {
            val note = hashMapOf<String, Any>(
                    "img" to R.drawable.rabbit,
                    "title" to "Lalala",
                    "content" to "内容啊啊啊啊啊奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥多付所付所多")
            noteList.add(note)
        }

        val noteAdapter = SimpleAdapter(
                this, noteList, R.layout.note_item,
                arrayOf("img", "title", "content"),
                intArrayOf(R.id.noteImg, R.id.noteTitle, R.id.noteContent)
        )
        noteRecyclerView.adapter = noteAdapter
        // 日历内容 & 响应事件
        val dayEvalList = ArrayList<CustomCalendar.DayEvaluation>()
        val nowCalendar = Calendar.getInstance()
        val monthFormat = SimpleDateFormat("yyyy-MM")
        nowCalendar.time = monthFormat.parse(monthFormat.format(Date()))
        for (i in 1..20) {
            val dayEval = CustomCalendar.DayEvaluation(nowCalendar.time, 3, 1)
            dayEvalList.add(dayEval)
            nowCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
//        calendar.setEvaluation(dayEvalList)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

}
