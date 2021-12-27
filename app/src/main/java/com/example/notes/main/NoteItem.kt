package com.example.notes.main

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.example.notes.data.CONST
import com.example.notes.data.Note

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
            Column {
                Text(
                    text = note.printBody(),
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
