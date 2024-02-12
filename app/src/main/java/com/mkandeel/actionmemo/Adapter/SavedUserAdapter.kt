package com.mkandeel.actionmemo.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mkandeel.actionmemo.Helper.ClickListener
import com.mkandeel.actionmemo.Helper.Constants.ID
import com.mkandeel.actionmemo.Helper.Constants.PASSWORD
import com.mkandeel.actionmemo.Helper.Constants.USERNAME
import com.mkandeel.actionmemo.Helper.HelperClass
import com.mkandeel.actionmemo.R
import com.mkandeel.actionmemo.Room.users.User

class SavedUserAdapter(
    private val context: Context,
    private var list: List<User>, private val listener: ClickListener
) : RecyclerView.Adapter<SavedUserAdapter.ViewHolder>() {

    private var helper: HelperClass = HelperClass(this.context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.saved_user_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        helper.display(list[position].img, holder.img)
        holder.txt_username.text = list[position].username
        holder.txt_mail.text = list[position].email
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newList: List<User>) {
        this.list = newList
        this.notifyDataSetChanged();
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView
        var txt_username: TextView
        var txt_mail: TextView

        init {
            img = itemView.findViewById(R.id.img)
            txt_username = itemView.findViewById(R.id.txt_username)
            txt_mail = itemView.findViewById(R.id.txt_mail)
            val extras: Bundle = Bundle()
            itemView.setOnClickListener {
                extras.putString(USERNAME, list[adapterPosition].username)
                extras.putString(PASSWORD, list[adapterPosition].password)
                extras.putString(ID, list[adapterPosition].id)
                listener.onItemClickListener(adapterPosition, extras)
            }
            itemView.setOnLongClickListener {
                extras.putString(PASSWORD, list[adapterPosition].password)
                extras.putString(ID, list[adapterPosition].id)
                listener.onLongItemClickListener(adapterPosition, extras)
                true
            }
        }
    }
}