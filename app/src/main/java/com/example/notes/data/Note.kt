package com.example.notes.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.UUID

@Entity
@Parcelize
data class Note(
    @PrimaryKey val uuid: UUID = UUID.randomUUID(),
    @ColumnInfo val title: String,
    @ColumnInfo val body: String,
) : Parcelable {
    fun isEmpty(): Boolean = title.isBlank() && body.isBlank()
}
