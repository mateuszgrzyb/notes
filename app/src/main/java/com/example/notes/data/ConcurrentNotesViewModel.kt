package com.example.notes.data

import android.app.*
import androidx.compose.runtime.*
import androidx.lifecycle.*
import kotlinx.coroutines.*

class ConcurrentNotesViewModel(
    app: Application
) : SavedNotesViewModel(app) {

    // state flows down
    var notes: MutableList<Note> = mutableStateListOf()

    var changed: Boolean by mutableStateOf(false)

    init {
        viewModelScope.launch {
            notes = loadDataAsync().await().toMutableList()
        }
    }

    suspend fun loadDataAsync(): Deferred<List<Note>> =
        viewModelScope.async { withContext(Dispatchers.Unconfined) { load() } }

    suspend fun saveDataAsync(data: List<Note>) =
        viewModelScope.launch { withContext(Dispatchers.Unconfined) { save(data) } }

    // events flow up
    fun addNote(note: Note) {
        notes += note
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
        notes -= tagged.toSet()
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
