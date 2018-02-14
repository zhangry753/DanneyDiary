package cn.zry.danneydiary.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import cn.zry.danneydiary.MyApplication
import cn.zry.danneydiary.R
import cn.zry.danneydiary.model.NoteEntity
import cn.zry.danneydiary.utils.DateUtils
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.note_item.*
import kotterknife.bindView

class EditActivity : AppCompatActivity() {
    private val dateText:TextView by bindView<TextView>(R.id.edit_date)
    private val titleText:TextView by bindView<TextView>(R.id.edit_title)
    private val contentText:TextView by bindView<TextView>(R.id.edit_content)
    private val commendCheck:RadioButton by bindView<RadioButton>(R.id.edit_commend)
    private val criticizeCheck:RadioButton by bindView<RadioButton>(R.id.edit_criticize)
    private val saveBtn:Button by bindView<Button>(R.id.edit_save)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val noteDao = (application as MyApplication).db.noteDao()
        //获取数据
        val note = Gson().fromJson(intent.extras.getString("data"), NoteEntity::class.java)
        dateText.text = DateUtils.date2Str(note.date)
        titleText.text = note.title
        contentText.text = note.content
        if(note.isCommend)
            commendCheck.isChecked = true
        else
            criticizeCheck.isChecked = true

        saveBtn.setOnClickListener { view ->
            note.title = titleText.text.toString()
            note.content = contentText.text.toString()
            if(commendCheck.isChecked)
                note.imageId = R.drawable.giraffe
            else if(criticizeCheck.isChecked)
                note.imageId = R.drawable.pig
            note.isCommend = commendCheck.isChecked
            // 表单验证
            if (note.title.isEmpty() && note.content.isEmpty()){
                Snackbar.make(view,"写点内容呗",Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // 执行插入或更新
            Observable.create(ObservableOnSubscribe<Long> { e ->
                val id = noteDao.insertOrUpdate(note)
                if (id.size > 0)
                    e.onNext(id[0])
                else
                    Toast.makeText(this,"保存失败",Toast.LENGTH_SHORT).show()
            }).subscribeOn(Schedulers.newThread())
              .subscribe({ id ->
                  System.out.println(id)
                  finish()
              })
        }
    }
}
