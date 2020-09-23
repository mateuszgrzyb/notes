package com.example.notes.main

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
    onRemoveNote: (Note) -> Unit,
    onEditNote: (Note) -> Unit,
    currentlyEdited: Note?,
    onChangeCurrentlyEdited: (Note?) -> Unit,
) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddNote() },
                icon = { Icons.Default.AddCircle },
            )
        },
    ) {
        NotesList(
            notes = notes,
            onRemoveNote = onRemoveNote,
            onEditNote = onEditNote,
            currentlyEdited = currentlyEdited,
            onChangeCurrentlyEdited = onChangeCurrentlyEdited
        )
    }
}


@Composable
fun NotesList(
    notes: List<Note>,
    onRemoveNote: (Note) -> Unit,
    onEditNote: (Note) -> Unit,
    currentlyEdited: Note?,
    onChangeCurrentlyEdited: (Note?) -> Unit,
) {

    Column(
        modifier = Modifier .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
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
                        NoteItem(
                            note = notePair.first!!,
                            onEditNote = onEditNote,
                            onRemoveNote = onRemoveNote,
                            currentlyEdited = currentlyEdited,
                            onChangeCurrentlyEdited = onChangeCurrentlyEdited,
                            size = size,
                            padding = padding,
                        )

                        if (notePair.second != null) {
                            NoteItem(
                                note = notePair.second!!,
                                onEditNote = onEditNote,
                                onRemoveNote = onRemoveNote,
                                currentlyEdited = currentlyEdited,
                                onChangeCurrentlyEdited = onChangeCurrentlyEdited,
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

