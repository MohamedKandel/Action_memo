package com.mkandeel.actionmemo.Room.users

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class User(
    @PrimaryKey
    var id:String,
    var username: String,
    var password:String,
    var email: String,
    var birthDate: String,
    var img:ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (username != other.username) return false
        if (password != other.password) return false
        if (email != other.email) return false
        if (birthDate != other.birthDate) return false
        if (!img.contentEquals(other.img)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + birthDate.hashCode()
        result = 31 * result + img.contentHashCode()
        return result
    }
}