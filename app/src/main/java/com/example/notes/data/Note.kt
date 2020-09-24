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
): Parcelable {

    fun isEmpty(): Boolean = title.isBlank() && body.isBlank()

    fun toJSON(): JSONObject = JSONObject(mapOf(
        GLOBAL.title to title,
        GLOBAL.body to body,
        GLOBAL.uuid to uuid.toString(),
    ))

    companion object {

        fun fromJSON(obj: JSONObject): Note = Note(
            title = obj.getString(GLOBAL.title),
            body = obj.getString(GLOBAL.body),
            uuid = UUID.fromString(obj.getString(GLOBAL.uuid))
        )

    }
}

object LIST {
    fun serializer(notes: List<Note>): String =
        JSONArray(notes.map { note -> note.toJSON() }).toString()

    fun deserializer(data: String): List<Note> =
        with(JSONArray(data)) {
            (0 until length()).map { i ->
                Note.fromJSON(optJSONObject(i))
            }
        }

    fun debug(): List<Note> =
        List(90) { randomNote() }
}


fun randomNote(): Note {
    val title = listOf(
        "Grocery List",
        "TODO:",
        "Best movies of all time",
        "Business Ideas",
        "books",
        "asdlfasdlkfjasld"
    ).random()

    val body = listOf(
        "qwerwqerqwer",
        "asdfasdfasdfasdf",
        "1324123412341234",
        "zxczxcvxzcvzxcv",
        "ala ma kota and so what?",
        "lorem ipsum",
    ).random()

    return Note(title, body)
}

