package com.example.notes.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.example.notes.data.CONST
import com.example.notes.data.Note
import java.util.UUID

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
                singleLine = true,
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
    singleLine: Boolean = false,
    size: Dp? = null
) {

    val newModifier = if (size != null) modifier.size(size)
    else modifier.fillMaxWidth()

    TextField(
        value = text,
        label = { Text(label) },
        onValueChange = onTextChange,
        modifier = newModifier
            .padding(CONST.PADDING)
            .background(Color.Transparent),
        singleLine = singleLine,
        shape = MaterialTheme.shapes.small,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        )
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
