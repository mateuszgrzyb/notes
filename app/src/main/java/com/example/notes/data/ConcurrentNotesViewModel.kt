package com.example.notes.data

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ConcurrentNotesViewModel(
    app: Application
) : SavedNotesViewModel(app) {

    // state flows down
    var notes = mutableStateListOf<Note>()

    private var changed: Boolean by mutableStateOf(false)

    init {
        viewModelScope.launch {
            notes += load()
        }
    }

    // events flow up
    private fun addNote(note: Note) {
        notes += note
        changed = true
    }

    private fun editNote(idx: Int, note: Note) {
        notes[idx] = note
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
        notes -= tagged.toSet()
        changed = true
    }

    fun saveNotes() {
        viewModelScope.launch {
            save(notes)
        }
    }

    override fun onCleared() {
        saveNotes()
        super.onCleared()
    }
}
