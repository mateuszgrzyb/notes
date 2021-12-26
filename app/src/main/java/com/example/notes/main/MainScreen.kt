package com.example.notes.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.notes.data.CONST
import com.example.notes.data.Note

@ExperimentalFoundationApi
@Composable
fun MainScreen(
    notes: List<Note>,
    onAddNote: () -> Unit,
    onEditNote: (Note) -> Unit,
    tagged: List<Note>,
    onTagNote: (Note) -> Unit,
    onRemoveTagged: () -> Unit,
    onCancelRemoval: () -> Unit,
) {
    val deleting = tagged.isNotEmpty()

    Scaffold(
        floatingActionButton = { if (!deleting) NotesFAB(onAddNote) },
        topBar = { if (deleting) NotesTopAppBar(tagged, onRemoveTagged, onCancelRemoval) }
    ) {
        NotesList(
            notes = notes,
            onEditNote = onEditNote,
            tagged = tagged,
            onTagNote = onTagNote,
        )
    }
}

@Composable
fun NotesTopAppBar(
    tagged: List<Note>,
    onRemoveTagged: () -> Unit,
    onCancelRemoval: () -> Unit,
) = TopAppBar(
    title = { Text("Deleting ${tagged.size} notes") },
    actions = {
        IconButton(
            onClick = onRemoveTagged,
        ) {
            Icon(Icons.Default.Delete, "")
        }
        IconButton(
            onClick = onCancelRemoval,
        ) {
            Icon(Icons.Default.Close, "")
        }
    }
)

@Composable
fun NotesFAB(
    onAddNote: () -> Unit,
) = FloatingActionButton(
    onClick = { onAddNote() },
) {
    Icon(Icons.Default.Add, "")
}

@ExperimentalFoundationApi
@Composable
fun NotesList(
    notes: List<Note>,
    onEditNote: (Note) -> Unit,
    tagged: List<Note>,
    onTagNote: (Note) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        BoxWithConstraints {
            val size = (maxWidth - CONST.PADDING * 2) / 2

            val newNotes = notes.reversed()

            Row {
                LazyVerticalGrid(
                    cells = GridCells.Fixed(2),
                    content = {
                        items(newNotes) {
                            note ->
                            NoteItem(
                                note = note,
                                onEditNote = onEditNote,
                                tagged = note in tagged,
                                tagging = tagged.isNotEmpty(),
                                onTagNote = { onTagNote(note) },
                                size = size,
                            )
                        }
                    }
                )
            }
        }
    }
}
