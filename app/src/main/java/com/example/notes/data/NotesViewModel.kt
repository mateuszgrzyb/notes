package com.example.notes.data

import android.app.*
import androidx.compose.runtime.*

class NotesViewModel(
    app: Application
): SavedViewModel<List<Note>>(
    app = app,
    init =
    //::listOf,
    LIST::debug,
    serializer = LIST::serializer,
    deserializer = LIST::deserializer,
) {


    // state flows down
    var notes: List<Note> by mutableStateOf(load())

    var changed: Boolean by mutableStateOf(false)

    // events flow up
    fun addNote(note: Note) {
        notes = notes + note
        changed = true
    }

    fun editNote(idx: Int, note: Note) {
        notes = notes.toMutableList().apply {
            this[idx] = note
        }
        changed = true
    }

    fun putNoteFromEditor(note: Note) {
        for ((idx, elem) in notes.withIndex()) {
            if (elem.uuid == note.uuid) {
                editNote(idx, note)
                return
            }
        }
        addNote(note)
        changed = true
    }

    fun removeNotes(tagged: List<Note>) {
        notes = notes - tagged
        changed = true
    }

    fun saveNotes() {
        if (changed) save(notes)
    }

    override fun onCleared() {
        saveNotes()
        super.onCleared()
    }
}
