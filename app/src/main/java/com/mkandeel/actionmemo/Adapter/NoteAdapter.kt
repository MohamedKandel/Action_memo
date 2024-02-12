package com.mkandeel.actionmemo.Adapter;

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mkandeel.actionmemo.Helper.ClickListener
import com.mkandeel.actionmemo.Helper.Constants
import com.mkandeel.actionmemo.Helper.Constants.CLICKED
import com.mkandeel.actionmemo.Helper.Constants.DELETE
import com.mkandeel.actionmemo.Helper.Constants.EDIT
import com.mkandeel.actionmemo.R
import com.mkandeel.actionmemo.Room.notes.Note

class NoteAdapter(
    private val context: Context,
    private var list: List<Note>,
    private val listener: ClickListener
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_layout, parent, false)
        return ViewHolder(view)
    }

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

        init {
            txt_title = itemView.findViewById<TextView>(R.id.note_title)
            txt_body = itemView.findViewById<TextView>(R.id.note_body)
            card = itemView.findViewById<CardView>(R.id.card_note)

            itemView.setOnClickListener {
                listener.onItemClickListener(adapterPosition, null)
            }
            itemView.setOnLongClickListener {
                val extras:Bundle = Bundle()
                val popupMenu = PopupMenu(context, itemView)
                popupMenu.menuInflater.inflate(R.menu.options_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener {
                    when(it.itemId) {
                        R.id.edit_note -> {
                            extras.putString(CLICKED, EDIT)
                            //Toast.makeText(context,context.resources.getString(R.string.edit), Toast.LENGTH_SHORT).show()
                        }
                        R.id.delete_note -> {
                            extras.putString(CLICKED, DELETE)
                            //Toast.makeText(context,context.resources.getString(R.string.delete_note), Toast.LENGTH_SHORT).show()
                        }
                    }
                    listener.onLongItemClickListener(adapterPosition, extras)
                    true
                }
                popupMenu.show()


                true
            }
        }
    }
}