package com.example.notes.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.*
import com.example.notes.data.*
import com.example.notes.test.*

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
