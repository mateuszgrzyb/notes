package com.example.notes.data

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class TaggedViewModel : ViewModel() {

    // state flows down
    val tagged: MutableList<Note> = mutableStateListOf()

    // events flow up
    fun tagNote(note: Note) {
        tagged.apply {
            if (note in this) remove(note)
            else add(note)
        }
    }

    fun clearTagged() {
        tagged.clear()
    }
}
