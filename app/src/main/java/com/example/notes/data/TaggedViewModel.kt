package com.example.notes.data

import androidx.compose.runtime.*
import androidx.lifecycle.*

class TaggedViewModel : ViewModel() {

    // state flows down
    val tagged: MutableList<Note> = mutableStateListOf()

    // events flow up
    fun tagNote(note: Note) {
        if (note in tagged) {
            tagged.remove(note)
        } else {
            tagged.add(note)
        }
        for (t in tagged) {
            println(t)
        }
    }

    fun clearTagged() {
        tagged.clear()
    }
}
