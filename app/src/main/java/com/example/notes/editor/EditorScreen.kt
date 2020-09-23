package com.example.notes.editor

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.input.*
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

    Column {

        NewTextField(
            text = title,
            onTextChange = changeTitle
        )
        NewTextField(
            text = body,
            onTextChange = changeBody,
        )

        Row {
            Button( { onNoteChange(Note(title, body, uuid)) } ) { Text("Apply") }
            Button(onActionBack) { Text("Back") }
        }
    }
}

@Composable
fun EditorTextField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onImeAction: () -> Unit = {}
) = TextField(
    value = text,
    onValueChange = onTextChange,
    label = { /* no label */ },
    backgroundColor = Color.Transparent,
    imeAction = ImeAction.Done,
    onImeActionPerformed = { action, softKeyBoardController ->
        if (action == ImeAction.Done) {
            onImeAction()
            softKeyBoardController?.hideSoftwareKeyboard()
        }
    },
    modifier = modifier
)

@Composable
fun NewTextField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        backgroundColor = Color.Transparent,
        modifier = modifier.fillMaxWidth()
    )
}




