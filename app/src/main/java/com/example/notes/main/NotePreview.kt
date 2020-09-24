package com.example.notes.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.gesture.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import com.example.notes.data.*

@Composable
fun NoteItem(
    note: Note,
    onEditNote: (Note) -> Unit,
    tagged: Boolean,
    tagging: Boolean,
    onTagNote: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    size: Dp,
    padding: Dp,
) {

    Card(
        modifier = Modifier
            .preferredSize(size)
            .padding(padding)
            .clickable(onClick = { if (tagging) onTagNote(!tagged) else onEditNote(note) } )
            .longPressGestureFilter { if (!tagging) onTagNote(!tagged) }
        ,
        backgroundColor = if (tagged) Color.Gray else Color.LightGray

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
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }

}

