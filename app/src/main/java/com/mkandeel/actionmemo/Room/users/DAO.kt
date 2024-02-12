package com.mkandeel.actionmemo.Room.users

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun registerUser(user: User)

    @Query("select id from users where username = :username and password = :password")
    suspend fun login(username: String, password: String): String

    @Query("select * from users")
    suspend fun getUsers(): List<User>

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("select id from users")
    suspend fun getAllIDs(): List<String>

    @Query("delete from users where id= :id")
    suspend fun deleteById(id: String)

    @Query("select * from users where id= :id")
    suspend fun getUserById(id: String): User

    @Query(
        "update users set username= :username, email= :email, birthDate= :birthDate," +
                "img= :img where id= :id"
    )
    suspend fun updateUserByID(
        username: String, email: String, birthDate: String,
        img: ByteArray, id: String
    )

    @Query("update users set lang= :lang where id= :id")
    suspend fun changeUserLang(lang: String, id: String)

    @Query("update users set uiMode= :uiMode where id= :id")
    suspend fun changeUserMode(uiMode: Boolean, id: String)
}