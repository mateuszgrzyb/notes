package com.example.notes.data

import android.app.Application
import com.example.notes.data.CONST.JSON
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

open class SavedNotesViewModel(app: Application) : SavedViewModel<List<Note>>(app) {

    override fun serialize(state: List<Note>): String =
        JSON.encodeToString<List<Note>>(state)

    override fun deserialize(data: String): List<Note> =
        JSON.decodeFromString<List<Note>>(data)

    override fun init(): List<Note> = listOf()
}
