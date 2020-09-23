package com.example.notes.data

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.*

class NotesViewModel(
    app: Application
): SavedViewModel<List<Note>>(
    app = app,
    init = {List(9) { randomNote() } },
    serializer = ::serializer,
    deserializer = ::deserializer,
) {

    // state flows down
    var notes: List<Note> by mutableStateOf(savePoint)

    var currentlyEdited: Note? by mutableStateOf(null)

    // events flow up
    fun addNote(note: Note) {
        notes = notes + note
    }

    fun removeNote(note: Note) {
        notes = notes.toMutableList().apply {
            remove(note)
        }
    }

    fun editNote(idx: Int, note: Note) {
        notes = notes.toMutableList().apply {
            this[idx] = note
        }
    }

    fun changeCurrentlyEdited(note: Note?) {
        currentlyEdited = note
    }

    fun currentlyEditedID(): Int =
        currentlyEdited?.let { notes.indexOf(it) } ?: -1

    fun putNoteFromEditor(note: Note) {
        for ((idx, elem) in notes.withIndex()) {
            if (elem == note) {
                editNote(idx, note)
                return
            }
        }
        addNote(note)
    }

    override fun onCleared() {
        savePoint = notes
        super.onCleared()
    }
}



fun serializer(notes: List<Note>): String =
    JSONArray(notes.map { note ->
        JSONObject(mapOf(
            GLOBAL.title to note.title,
            GLOBAL.body to note.body,
            GLOBAL.uuid to note.uuid.toString(),
        ))
    }).toString()

fun deserializer(data: String): List<Note> =
    with(JSONArray(data)) {
        (0 until length()).map { i ->
            with(optJSONObject(i)) {
                Note(
                    getString(GLOBAL.title),
                    getString(GLOBAL.body),
                    UUID.fromString(getString(GLOBAL.uuid)),
                )
            }
        }
    }


