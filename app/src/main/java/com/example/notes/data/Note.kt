package com.example.notes.data

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class Note(
    val title: String,
    val body: String,
    val uuid: UUID = UUID.randomUUID()
): Parcelable {
    constructor(parcel: Parcel): this(
        parcel.readString()!!,
        parcel.readString()!!,
        UUID.fromString(parcel.readString()!!)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(body)
        parcel.writeString(uuid.toString())
    }

    override fun describeContents(): Int = 0

    companion object CREATOR: Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note = Note(parcel)
        override fun newArray(size: Int): Array<Note?> = arrayOfNulls(size)
    }

    fun isEmpty(): Boolean = title.isBlank() && body.isBlank()
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
        /*
        "asdfasdfasdfasdf",
        "1324123412341234",
        "zxczxcvxzcvzxcv",
        "ala ma kota and so what?",
        "lorem ipsum",
         */
    ).random()

    return Note(title, body)
}

fun noteSerializer(note: Note): String = TODO()
fun noteDeserializer(str: String): Note = TODO()
