package com.example.notes.data

import android.app.Application
import org.json.JSONArray

open class SavedNotesViewModel(app: Application) : SavedViewModel<List<Note>>(app) {

    override fun serialize(state: List<Note>): String =
        JSONArray(state.map { note -> note.toJSON() }).toString()

    override fun deserialize(data: String): List<Note> =
        with(JSONArray(data)) {
            (0 until length()).map { i ->
                Note.fromJSON(optJSONObject(i))
            }
        }

    override fun init(): List<Note> = listOf()
}
