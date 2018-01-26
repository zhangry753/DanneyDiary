package zry.cn.danneydiary

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

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
        // 加载note
        val noteList = ArrayList<Map<String, Any>>()
        for (i in 0..10) {
            val note = hashMapOf<String, Any>(
                    "img" to R.drawable.rabbit,
                    "title" to "Lalala",
                    "content" to "内容啊啊啊啊啊奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥奥多付所付所多")
            noteList.add(note)
        }
        val noteRecyclerView = findViewById<ListView>(R.id.noteRecyclerView)
        val noteAdapter = SimpleAdapter(
                this, noteList, R.layout.note_item,
                arrayOf("img", "title", "content"),
                intArrayOf(R.id.noteImg, R.id.noteTitle, R.id.noteContent)
        )
        noteRecyclerView.adapter=noteAdapter
        // 日历内容 & 响应事件
        val calendar = findViewById<CustomCalendar>(R.id.calendar)
        val dayEvalList = ArrayList<CustomCalendar.DayEvaluation>()
        for(i in 1..20){
            val dayEval = CustomCalendar.DayEvaluation(i,3,1)
            dayEvalList.add(dayEval)
        }
        calendar.setEvaluation(dayEvalList)

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
