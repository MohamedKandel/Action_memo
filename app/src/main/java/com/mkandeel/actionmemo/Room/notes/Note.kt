package com.mkandeel.actionmemo.Room.notes

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.mkandeel.actionmemo.Room.users.User
import kotlinx.android.parcel.Parcelize

@Entity(
    tableName = "notes",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE // Define what happens on user deletion
    )]
)
@Parcelize
class Note(
    @ColumnInfo(name = "userId", index = true)
    var userId: String,
    var title: String,
    var body: String,
    var priority: Int
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Note

        if (id != other.id) return false
        if (userId != other.userId) return false
        if (title != other.title) return false
        if (body != other.body) return false
        if (priority != other.priority) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + body.hashCode()
        result = 31 * result + priority.hashCode()
        return result
    }
}