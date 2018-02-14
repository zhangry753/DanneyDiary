package cn.zry.danneydiary.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.*
import cn.zry.danneydiary.MyApplication
import cn.zry.danneydiary.widget.CustomCalendar
import cn.zry.danneydiary.R
import cn.zry.danneydiary.adapter.NoteAdapter
import cn.zry.danneydiary.dao.NoteDao
import cn.zry.danneydiary.model.NoteEntity
import cn.zry.danneydiary.utils.DateUtils
import cn.zry.danneydiary.utils.FileUtils
import cn.zry.danneydiary.utils.PermissionUtils
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_main.*
import kotterknife.bindView
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val noteListView: ListView by bindView(R.id.noteListView)
    private val calendar: CustomCalendar by bindView(R.id.calendar)

    private lateinit var noteDao: NoteDao
    val noteList = ArrayList<NoteEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra("data", Gson().toJson(NoteEntity(date = Date())))
            startActivityForResult(intent, 1)
        }

        noteDao = (application as MyApplication).db.noteDao()

        // note列表
        val popupWindowView = this.layoutInflater.inflate(R.layout.popup_window, null)
        val popupWindow = PopupWindow(popupWindowView,
          ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT,
          true)
        popupWindow.setAnimationStyle(R.style.popupAnimation);
        val deleteBtn = popupWindowView.findViewById<Button>(R.id.popup_confirmBtn)
        val cancelBtn = popupWindowView.findViewById<Button>(R.id.popup_cancelBtn)
        cancelBtn.setOnClickListener { view -> popupWindow.dismiss() }
        // note列表点击事件
        val noteClickListener = object : NoteAdapter.ClickListener {
            // 点击打开编辑
            override fun onClick(view: View, note: NoteEntity) {
                val intent = Intent(this@MainActivity, EditActivity::class.java)
                intent.putExtra("data", Gson().toJson(note))
                this@MainActivity.startActivityForResult(intent, 1)
            }

            // 长按删除
            override fun onLongClick(view: View, note: NoteEntity) {
                deleteBtn.setOnClickListener { view ->
                    Observable.create(ObservableOnSubscribe<Unit> { e ->
                        noteDao.delete(note)
                        e.onNext(Unit)
                    }).subscribeOn(Schedulers.newThread())
                      .observeOn(AndroidSchedulers.mainThread())
                      .subscribe({ value ->
                          Toast.makeText(this@MainActivity, "删除成功", Toast.LENGTH_SHORT).show()
                          calendar.refresh()
                      })
                    popupWindow.dismiss()
                }
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
            }
        }
        this.noteListView.adapter = NoteAdapter(this, noteList, noteClickListener)

        // 日历响应事件
        this.calendar.setOnClickListener(object : CustomCalendar.simpleOnClickListener(calendar) {
            // 日期点击事件
            override fun onDayClick(dayIndex: Int, dayStr: String, dayEval: CustomCalendar.DayEvaluation?) {
                super.onDayClick(dayIndex, dayStr, dayEval)
                noteList.clear()
                Observable.create(ObservableOnSubscribe<Unit> { e ->
                    noteList.addAll(noteDao.getByDay(dayStr))
                    e.onNext(Unit)
                }).subscribeOn(Schedulers.newThread())
                  .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    (noteListView.adapter as NoteAdapter).notifyDataSetChanged()
                })
            }

            // 加载每月中的数据
            override fun loadData(monthStr: String) {
                super.loadData(monthStr)
                Observable.create(ObservableOnSubscribe<Map<Long, CustomCalendar.DayEvaluation>> { e ->
                    val noteList = noteDao.getByDay(monthStr)
                    val dateDataMap = noteList.groupBy { it ->
                        DateUtils.date2Date(it.date).time
                    }.mapValues { entry ->
                        CustomCalendar.DayEvaluation(
                          Date(entry.key),
                          entry.value.filter { it.isCommend }.count(),
                          entry.value.filter { !it.isCommend }.count()
                        )
                    }
                    e.onNext(dateDataMap)
                }).subscribeOn(Schedulers.newThread())
                  .observeOn(AndroidSchedulers.mainThread()).subscribe { data ->
                    calendar.setEvaluation(data)
                }
            }
        })
        this.calendar.initData()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        calendar.refresh()
    }

    /***
     * 右上角的菜单
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_backup -> {
                if(!PermissionUtils.externalStorage(this))
                    return true
                val fileCount = FileUtils.copy(
                  getDatabasePath("danneyDiary").path,
                  Environment.getExternalStorageDirectory().path + "/danneyDiary"
                )
                if (fileCount > 0)
                    Toast.makeText(this, "备份成功", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, "备份失败", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_restore -> {
                AlertDialog.Builder(this)
                  .setMessage("确认还原数据么？")
                  .setPositiveButton("取消", null)
                  .setNegativeButton("确认", { dialog, which ->
                      if(!PermissionUtils.externalStorage(this))
                          return@setNegativeButton
                      val fileCount = FileUtils.copy(
                        Environment.getExternalStorageDirectory().path + "/danneyDiary",
                        getDatabasePath("danneyDiary").path
                      )
                      if (fileCount > 0)
                          Toast.makeText(this, "还原成功", Toast.LENGTH_SHORT).show()
                      else
                          Toast.makeText(this, "还原失败", Toast.LENGTH_SHORT).show()
                      calendar.refresh()
                  }).show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
