package com.example.notes.data

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable
import java.io.File
import java.util.UUID

@Serializable
sealed class Note : Parcelable {

    abstract val title: String
    abstract val body: Any?
    abstract val uuid: UUID

    fun isEmpty(): Boolean = title.isBlank() && bodyIsEmpty()
    abstract fun printBody(): String

    protected abstract fun bodyIsEmpty(): Boolean
}

@Parcelize
@Immutable
@Serializable
data class TextNote(
    override val title: String,
    override val body: String,
    override val uuid: @Serializable(with = UUIDSerializer::class) UUID = UUID.randomUUID()
) : Note(), Parcelable {
    constructor() : this("", "")

    override fun bodyIsEmpty(): Boolean = body.isEmpty()
    override fun printBody(): String = body
}

@Parcelize
@Immutable
@Serializable
data class ListNote(
    override val title: String,
    override val body: List<String>,
    override val uuid: @Serializable(with = UUIDSerializer::class) UUID = UUID.randomUUID()
) : Note(), Parcelable {
    constructor() : this("", listOf())

    override fun bodyIsEmpty(): Boolean = body.isEmpty()
    override fun printBody(): String =
        body.joinToString(separator = System.lineSeparator()) { elem -> "• $elem" }
}

@Parcelize
@Immutable
@Serializable
data class VoiceNote(
    override val title: String,
    override val body: @Serializable(with = FileSerializer::class) File?,
    override val uuid: @Serializable(with = UUIDSerializer::class) UUID = UUID.randomUUID()
) : Note(), Parcelable {
    constructor() : this("", null)

    override fun printBody(): String = "Voice recording"
    override fun bodyIsEmpty(): Boolean = (body == null)
}
