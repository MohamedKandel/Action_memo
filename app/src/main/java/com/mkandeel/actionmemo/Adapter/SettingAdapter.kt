package com.mkandeel.actionmemo.Adapter;

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mkandeel.actionmemo.Helper.ClickListener
import com.mkandeel.actionmemo.R
import com.mkandeel.actionmemo.data.SettingModel

class SettingAdapter(
    private val context: Context,
    private var list: List<SettingModel>,
    private val listener: ClickListener
) : RecyclerView.Adapter<SettingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.setting_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.img.setImageResource(list[position].imgSrc)
        holder.txt.text = list[position].txt
        holder.imgGo.setImageResource(R.drawable.arrow)
        if(position == list.size-1 || position==list.size-2) {
            holder.txt.setTextColor(Color.RED)
            holder.img.setColorFilter(Color.RED)
            holder.imgGo.setColorFilter(Color.RED)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(newList: List<SettingModel>) {
        this.list = newList
        this.notifyDataSetChanged();
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img:ImageView
        var txt:TextView
        var imgGo:ImageView
        init {
            img= itemView.findViewById(R.id.img_icon)
            txt= itemView.findViewById(R.id.txt)
            imgGo = itemView.findViewById(R.id.img_go)

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