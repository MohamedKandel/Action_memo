package com.mkandeel.actionmemo.Room.notes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("select * from notes where userId= :userId")
    suspend fun getNotes(userId: String): List<Note>

    @Query("update notes set title= :title, body= :body, priority= :priority where id = :id")
    suspend fun updateNoteByID(title: String, body: String, priority: Int, id: Int)

    @Query("delete from notes where userId= :userId")
    suspend fun deleteAllNotes(userId: String)
}