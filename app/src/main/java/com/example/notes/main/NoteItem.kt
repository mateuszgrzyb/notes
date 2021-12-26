package com.example.notes.main

import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import com.example.notes.data.*

@Composable
fun NoteItem(
    note: Note,
    onEditNote: (Note) -> Unit,
    tagged: Boolean,
    tagging: Boolean,
    onTagNote: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp,
) {

    Card(
        modifier = Modifier
            .size(size)
            .padding(CONST.PADDING)
            .pointerInput(tagging) {
                detectTapGestures(
                    onTap = {
                        if (tagging) onTagNote() else onEditNote(note)
                    },
                    onLongPress = {
                        if (!tagging) onTagNote()
                    },
                )
            },
        backgroundColor = if (tagged) Color.Gray else Color.LightGray

    ) {
        Column(
            modifier = modifier
                .padding(CONST.PADDING)
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.h1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            // Stack {
            Column {
                Text(
                    text = note.body,
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
