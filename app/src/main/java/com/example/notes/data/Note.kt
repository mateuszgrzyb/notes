package com.example.notes.data

import android.os.*
import kotlinx.android.parcel.*
import org.json.*
import java.util.*

@Parcelize
data class Note(
    val title: String,
    val body: String,
    val uuid: UUID = UUID.randomUUID()
) : Parcelable {

    fun isEmpty(): Boolean = title.isBlank() && body.isBlank()

    fun toJSON(): JSONObject = JSONObject(
        mapOf(
            CONST.TITLE to title,
            CONST.BODY to body,
            CONST.UUID to uuid.toString(),
        )
    )

    companion object {
        fun fromJSON(obj: JSONObject): Note = Note(
            title = obj.getString(CONST.TITLE),
            body = obj.getString(CONST.BODY),
            uuid = UUID.fromString(obj.getString(CONST.UUID))
        )
    }
}
