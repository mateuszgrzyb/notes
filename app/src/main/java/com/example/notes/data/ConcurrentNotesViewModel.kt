package com.example.notes.data

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.db.DB
import kotlinx.coroutines.launch

class ConcurrentNotesViewModel(
    app: Application
) : AndroidViewModel(app) {
    // state flows down
    private val db = DB(app)

    var notes: MutableList<Note> = mutableStateListOf()

    init {
        viewModelScope.launch {
            notes = db.loadNotes().toMutableList()
        }
    }

    // events flow up
    fun addNote(note: Note) {
        notes += note
    }

    fun editNote(idx: Int, note: Note) {
        notes = notes.toMutableList().apply {
            this[idx] = note
        }
    }

    fun putNoteFromEditor(note: Note) {
        for ((idx, elem) in notes.withIndex()) {
            if (elem.uuid == note.uuid) {
                editNote(idx, note)
                return
            }
        }
        addNote(note)
    }

    fun removeNotes(tagged: List<Note>) {
        notes -= tagged.toSet()
    }

    override fun onCleared() {
        save()
        super.onCleared()
    }

    fun save() {
        viewModelScope.launch {
            db.saveNotes(notes)
        }
    }
}
