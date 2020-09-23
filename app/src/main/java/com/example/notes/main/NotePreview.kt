package com.example.notes.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.ui.tooling.preview.*
import com.example.notes.data.*

@Composable
fun NoteItem(
    note: Note,
    onEditNote: (Note) -> Unit,
    onRemoveNote: (Note) -> Unit,
    currentlyEdited: Note?,
    onChangeCurrentlyEdited: (Note?) -> Unit,
    modifier: Modifier = Modifier,
    size: Dp,
    padding: Dp,
) {

    val edit = note == currentlyEdited


    Card(
        modifier = Modifier
            .preferredSize(size)
            .padding(padding)
            .clickable(onClick = { onChangeCurrentlyEdited(
                if (edit) null
                else note
            ) } )
        ,
        backgroundColor = Color.Gray

    ) {
        Column(
            modifier = modifier
                .padding(padding)
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.h1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Stack {
                Text(
                    text = note.body,
                    style = MaterialTheme.typography.body1,
                    maxLines = 9,
                    overflow = TextOverflow.Ellipsis,
                )
                if (edit) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Button(
                            onClick = { onEditNote(note) },
                        ) {
                            Icon(Icons.Default.Edit)
                        }
                        Button(
                            onClick = { onRemoveNote(note) }
                        ) {
                            Icon(Icons.Default.Delete)
                        }
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true, showDecoration = true)
@Composable
fun PreviewNoteItem() {

    val note = remember { Note("Ala", "ma") }

    val (current, changeCurrent) = remember { mutableStateOf<Note?>(null) }



    WithConstraints {
    val size = with(DensityAmbient.current) {
        constraints.maxWidth.toDp() / 2
    }



        Row {

            NoteItem(
                note = note,
                onEditNote = { println("edit note") },
                onRemoveNote = { println("remove note") },
                currentlyEdited = current,
                onChangeCurrentlyEdited = changeCurrent,
                size = size,
                padding = 8.dp
            )
            NoteItem(
                note = note,
                onEditNote = { println("edit note") },
                onRemoveNote = { println("remove note") },
                currentlyEdited = current,
                onChangeCurrentlyEdited = changeCurrent,
                size = size,
                padding = 8.dp
            )
        }
    }

}

