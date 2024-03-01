package com.mkandeel.actionmemo.Adapter;

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mkandeel.actionmemo.Helper.ClickListener
import com.mkandeel.actionmemo.Helper.Constants
import com.mkandeel.actionmemo.Helper.Constants.CLICKED
import com.mkandeel.actionmemo.Helper.Constants.DELETE
import com.mkandeel.actionmemo.Helper.Constants.EDIT
import com.mkandeel.actionmemo.Helper.Constants.NOTE
import com.mkandeel.actionmemo.Helper.HelperClass
import com.mkandeel.actionmemo.R
import com.mkandeel.actionmemo.Room.notes.Note


class NoteAdapter(
    private val context: Context,
    private var list: List<Note>,
    private val listener: ClickListener,
    private val helper: HelperClass
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_layout, parent, false)
        return ViewHolder(view)
    }

    /*
    في صباح اليوم ذهبن الى الحضانة و كنت عايز انام نفسي يجي اليوم اللي ماروحش فيه الحضانة بس اقبض برضه
     */

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = list[position]
        holder.txt_title.text = note.title
        holder.txt_body.text = note.body

        Constants.colors[note.priority]?.let {
            holder.card.setCardBackgroundColor(it)
        }
        changeHoleColor(helper.getTheme(), holder.img1, holder.img2, holder.img3)
    }

    private fun changeHoleColor(isDark: Boolean, vararg imgs: ImageView) {
        if (isDark) {
            for (img in imgs) {
                img.setColorFilter(Color.rgb(18, 18, 18))
            }
        } else {
            for (img in imgs) {
                img.setColorFilter(Color.rgb(255, 255, 255))
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(newList: List<Note>) {
        this.list = newList
        this.notifyDataSetChanged();
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_title: TextView
        var txt_body: TextView
        var card: CardView
        var img1: ImageView
        var img2: ImageView
        var img3: ImageView

        init {
            txt_title = itemView.findViewById(R.id.note_title)
            txt_body = itemView.findViewById(R.id.note_body)
            card = itemView.findViewById(R.id.card_note)
            img1 = itemView.findViewById(R.id.hole_1)
            img2 = itemView.findViewById(R.id.hole_2)
            img3 = itemView.findViewById(R.id.hole_3)


            val bundle = Bundle()

            itemView.setOnClickListener {
                val noteObj = list[adapterPosition]
                bundle.putParcelable(NOTE, noteObj)
                listener.onItemClickListener(adapterPosition, bundle)
            }
            itemView.setOnLongClickListener {
                val extras: Bundle = Bundle()
                val popupMenu = PopupMenu(context, itemView)
                popupMenu.menuInflater.inflate(R.menu.options_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.edit_note -> {
                            extras.putString(CLICKED, EDIT)
                            //Toast.makeText(context,context.resources.getString(R.string.edit), Toast.LENGTH_SHORT).show()
                        }

                        R.id.delete_note -> {
                            extras.putString(CLICKED, DELETE)
                            //Toast.makeText(context,context.resources.getString(R.string.delete_note), Toast.LENGTH_SHORT).show()
                        }
                    }
                    extras.putParcelable(NOTE, list[adapterPosition])
                    listener.onLongItemClickListener(adapterPosition, extras)
                    true
                }
                popupMenu.show()


                true
            }
        }
    }
}