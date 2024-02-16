package com.mkandeel.actionmemo.Adapter;

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mkandeel.actionmemo.Helper.ClickListener
import com.mkandeel.actionmemo.R
import com.mkandeel.actionmemo.data.Apps

class AppAdapter(
    private val context: Context,
    private var list: List<Apps>,
    private val listener: ClickListener
) : RecyclerView.Adapter<AppAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.apps_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val app = list[position]
        holder.img.setImageResource(app.icon)
        holder.name.setText(app.name)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(newList: List<Apps>) {
        this.list = newList
        this.notifyDataSetChanged();
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView
        var name: TextView

        init {
            img = itemView.findViewById(R.id.app_icon)
            name = itemView.findViewById(R.id.txt_app_name)

            itemView.setOnClickListener {
                listener.onItemClickListener(adapterPosition, null)
            }
            itemView.setOnLongClickListener {
                listener.onLongItemClickListener(adapterPosition, null)
                true
            }
        }
    }
}