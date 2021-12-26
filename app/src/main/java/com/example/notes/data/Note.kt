package com.example.notes.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable
import java.util.UUID

@Parcelize
@Serializable
data class Note(
    val title: String,
    val body: String,
    val uuid: @Serializable(with = UUIDSerializer::class) UUID = UUID.randomUUID()
) : Parcelable {
    fun isEmpty(): Boolean = title.isBlank() && body.isBlank()
}
