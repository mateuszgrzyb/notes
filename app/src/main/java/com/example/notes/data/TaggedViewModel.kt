package com.example.notes.data

import androidx.compose.runtime.*
import androidx.lifecycle.*

class TaggedViewModel: ViewModel() {

    // state flows down
    var tagged: List<Note> by mutableStateOf(listOf())

    // events flow up
    fun tagNote(note: Note, tag: Boolean) {
        tagged = tagged.toMutableList().apply {
            if (tag) add(note)
            else remove(note)
        }
    }

    fun clearTagged() {
        tagged = listOf()
    }

}

