package com.mkandeel.actionmemo.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mkandeel.actionmemo.Room.notes.Note
import com.mkandeel.actionmemo.Room.users.User

@Database(entities = [User::class, Note::class], version = 1)
abstract class NotesDB : RoomDatabase() {
    abstract fun userDAO(): com.mkandeel.actionmemo.Room.users.DAO
    abstract fun noteDAO(): com.mkandeel.actionmemo.Room.notes.DAO

    companion object {
        @Volatile
        private var INSTANCE: NotesDB? = null
        fun getDBInstace(context: Context): NotesDB {
            val tempInstace = INSTANCE
            if (tempInstace != null) {
                return tempInstace
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDB::class.java,
                    "notesDB"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}