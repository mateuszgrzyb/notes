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


@Composable
fun MainScreen(
    notes: List<Note>,
    onAddNote: () -> Unit,
    onEditNote: (Note) -> Unit,
    tagged: List<Note>,
    onTagNote: (Note, Boolean) -> Unit,
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
            icon = { Icon(Icons.Default.Delete) }
        )
        IconButton(
            onClick = onCancelRemoval,
            icon = { Icon(Icons.Default.Close) }
        )
    }
)

@Composable
fun NotesFAB(
    onAddNote: () -> Unit,
) = FloatingActionButton(
    onClick = { onAddNote() },
    icon = { Icon(Icons.Default.Add) },
)

@Composable
fun NotesList(
    notes: List<Note>,
    onEditNote: (Note) -> Unit,
    tagged: List<Note>,
    onTagNote: (Note, Boolean) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        WithConstraints {
            val padding = 8.dp
            val size = with(DensityAmbient.current) {
                (constraints.maxWidth.toDp() - padding * 2) / 2
            }
            val newNotes = notes.reversed().splitToPairs()
            Row {
                LazyColumnForIndexed(
                    items = newNotes,
                ) { idx, notePair ->
                    Row(
                        modifier = Modifier.paddingOfRow(idx, newNotes.size - 1, padding)
                    ) {
                        notePair.first!!.also { first ->
                            NoteItem(
                                note = first,
                                onEditNote = onEditNote,
                                tagged = first in tagged,
                                tagging = tagged.isNotEmpty(),
                                onTagNote = { onTagNote(first, it) },
                                size = size,
                                padding = padding,
                            )
                        }

                        notePair.second?.also { second ->
                            NoteItem(
                                note = second,
                                onEditNote = onEditNote,
                                tagged = second in tagged,
                                tagging = tagged.isNotEmpty(),
                                onTagNote = { onTagNote(second, it) },
                                size = size,
                                padding = padding
                            )
                        }
                    }
                }
            }
        }
    }
}

fun Modifier.paddingOfRow(
    idx: Int,
    maxIdx: Int,
    padding: Dp,
): Modifier = when (idx) {
    0 -> padding(top = padding, start = padding, end = padding)
    maxIdx -> padding(start = padding, bottom = padding, end = padding)
    else -> padding(start = padding, end = padding)
}

