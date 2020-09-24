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
    val padding = 8.dp

    Scaffold(
        topBar = { TopAppBar(title = { Text ("Editing") } ) }
    ) {

        Column(modifier = Modifier.padding(padding)) {

            EditorTextField(
                text = title,
                onTextChange = changeTitle,
                label = "Title",
                padding = padding
            )
            WithConstraints {
                val size = with(DensityAmbient.current) { constraints.maxWidth.toDp() }
                EditorTextField(
                    text = body,
                    onTextChange = changeBody,
                    size = size,
                    label = "Body",
                    padding = padding
                )
            }

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.End
            ) {
                EditorButton(
                    onClick = { onNoteChange(Note(title, body, uuid)) },
                    text = "Apply",
                    padding = padding,
                )
                EditorButton(
                    onClick = onActionBack,
                    text = "Back",
                    padding = padding,
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
    padding: Dp,
    modifier: Modifier = Modifier,
    size: Dp? = null
) {

    val newModifier = if (size != null) modifier.preferredSize(size)
                      else modifier.fillMaxWidth()

    TextField(
        value = text,
        label = { Text(label) },
        onValueChange = onTextChange,
        modifier = newModifier.padding(padding).background(Color.Transparent),
    )
}

@Composable
fun EditorButton(
    onClick: () -> Unit,
    text: String,
    padding: Dp
) = Button(
    onClick = onClick,
    modifier = Modifier.padding(padding)
) {
    Text(text)
}




