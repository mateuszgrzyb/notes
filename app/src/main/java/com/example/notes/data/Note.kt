package com.example.notes.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
sealed class Note : Parcelable {

    abstract val title: String
    abstract val body: Any
    val uuid: @Serializable(with = UUIDSerializer::class) UUID = UUID.randomUUID()

    fun isEmpty(): Boolean = title.isBlank() && bodyIsEmpty()
    abstract fun printBody(): String

    protected abstract fun bodyIsEmpty(): Boolean
}

@Parcelize
@Serializable
data class TextNote(
    override val title: String,
    override val body: String,
) : Note(), Parcelable {
    override fun bodyIsEmpty(): Boolean = body.isEmpty()
    override fun printBody(): String = body
}

@Parcelize
@Serializable
data class ListNote(
    override val title: String,
    override val body: List<String>,
) : Note(), Parcelable {
    override fun bodyIsEmpty(): Boolean = body.isEmpty()
    override fun printBody(): String =
        body.joinToString(separator = System.lineSeparator()) { elem -> "• $elem" }
}
