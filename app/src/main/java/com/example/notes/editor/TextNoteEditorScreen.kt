package com.example.notes.editor

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.notes.data.TextNote
import java.util.UUID

@Composable
fun TextNoteEditorScreen(
    note: TextNote?,
    onNoteChange: (TextNote?) -> Unit,
) {

    val (title, changeTitle) = remember { mutableStateOf(note?.title ?: "") }
    val (body, changeBody) = remember { mutableStateOf(note?.body ?: "") }
    val uuid = remember { note?.uuid ?: UUID.randomUUID() }

    BaseNoteEditorScreen(
        title = title,
        changeTitle = changeTitle,
        onNoteChange = {
            onNoteChange(TextNote(title, body, uuid))
        },
        onActionBack = { onNoteChange(null) },
    ) {
        BoxWithConstraints {
            val size = maxWidth
            EditorTextField(
                text = body,
                onTextChange = changeBody,
                size = size,
                label = "Body",
            )
        }
    }
}
