package com.example.notes.editor

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.*
import com.example.notes.data.*
import java.util.*

@Composable
fun EditorScreen(
    note: Note?,
    onNoteChange: (Note) -> Unit,
    onActionBack: () -> Unit,
) {

    val (title, changeTitle) = remember { mutableStateOf(note?.title ?: "") }
    val (body, changeBody) = remember { mutableStateOf(note?.body ?: "") }
    val uuid = remember { note?.uuid ?: UUID.randomUUID() }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Editing") }) }
    ) {

        Column(modifier = Modifier.padding(CONST.PADDING)) {

            EditorTextField(
                text = title,
                onTextChange = changeTitle,
                label = "Title",
            )
            BoxWithConstraints {
                val size = maxWidth
                EditorTextField(
                    text = body,
                    onTextChange = changeBody,
                    size = size,
                    label = "Body",
                )
            }

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.End
            ) {
                EditorButton(
                    onClick = onActionBack,
                    text = "Back",
                )
                EditorButton(
                    onClick = { onNoteChange(Note(title, body, uuid)) },
                    text = "Apply",
                )
            }
        }
    }
}

@Composable
fun EditorTextField(
    text: String,
    onTextChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    size: Dp? = null
) {

    val newModifier = if (size != null) modifier.size(size)
    else modifier.fillMaxWidth()

    TextField(
        value = text,
        label = { Text(label) },
        onValueChange = onTextChange,
        modifier = newModifier.padding(CONST.PADDING).background(Color.Transparent),
    )
}

@Composable
fun EditorButton(
    onClick: () -> Unit,
    text: String,
) = Button(
    onClick = onClick,
    modifier = Modifier.padding(CONST.PADDING)
) {
    Text(text)
}
