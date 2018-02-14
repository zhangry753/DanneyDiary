package cn.zry.danneydiary.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.*
import android.widget.*
import cn.zry.danneydiary.R
import cn.zry.danneydiary.activity.EditActivity
import cn.zry.danneydiary.model.NoteEntity
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers

/**
 * Created by zry on 2018/2/2.
 */
class NoteAdapter(val context: Activity, val noteList: List<NoteEntity>, val listener:ClickListener) : BaseAdapter() {
    private val mInflater: LayoutInflater = context.layoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: mInflater.inflate(R.layout.note_item, null)
        val viewHolder: ViewHolder = (view.tag ?: ViewHolder(
          view.findViewById<ImageView>(R.id.noteImg),
          view.findViewById<TextView>(R.id.noteTitle),
          view.findViewById<TextView>(R.id.noteContent)
        )) as ViewHolder
        val note = noteList.get(position)
        viewHolder.imgView.setImageResource(note.imageId)
        viewHolder.titleText.text = note.title
        viewHolder.contentText.text = note.content
        view.setOnClickListener { view ->
            listener.onClick(view, note)
        }
        view.setOnLongClickListener { view ->
            listener.onLongClick(view, note)
            return@setOnLongClickListener true
        }
        return view
    }

    override fun getItem(position: Int): NoteEntity {
        return noteList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return noteList.get(position).id ?: -1
    }

    override fun getCount(): Int {
        return noteList.size
    }


    internal class ViewHolder(
      val imgView: ImageView,
      val titleText: TextView,
      val contentText: TextView
    )

    interface ClickListener{
        fun onClick(view:View, note:NoteEntity)
        fun onLongClick(view:View, note:NoteEntity)
    }
}


