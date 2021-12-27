package com.example.notes.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Check
import androidx.compose.material.icons.sharp.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import com.example.notes.data.CONST
import com.example.notes.data.ListNote
import com.example.notes.data.TextNote
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun TextNoteEditorScreen(
    note: TextNote?,
    onNoteChange: (TextNote?) -> Unit,
) {

    val (title, changeTitle) = remember { mutableStateOf(note?.title ?: "") }
    val (body, changeBody) = remember { mutableStateOf(note?.body ?: "") }

    BaseNoteEditorScreen(
        title = title,
        changeTitle = changeTitle,
        onNoteChange = { onNoteChange(TextNote(title, body)) },
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
@Composable
fun BaseNoteEditorScreen(
    title: String,
    changeTitle: (String) -> Unit,
    onNoteChange: () -> Unit,
    onActionBack: () -> Unit,
    content: @Composable () -> Unit,
) {
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
            content()

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.End
            ) {
                EditorButton(
                    onClick = onActionBack,
                    text = "Back",
                )
                EditorButton(
                    onClick = onNoteChange,
                    text = "Apply",
                )
            }
        }
    }
}

@Composable
fun ListNoteEditorScreen(
    note: ListNote?,
    onNoteChange: (ListNote?) -> Unit,
) {

    val (title, changeTitle) = remember { mutableStateOf(note?.title ?: "") }
    val list = remember {
        mutableStateListOf(
            *(note?.body ?: listOf()).toTypedArray(), ""
        )
    }

    BaseNoteEditorScreen(
        title = title,
        changeTitle = changeTitle,
        onNoteChange = { list.removeLast(); onNoteChange(ListNote(title, list)) },
        onActionBack = { onNoteChange(null) },
    ) {
        Column {
            for ((i, e) in list.withIndex()) {
                ListNoteElemTextField(
                    text = e,
                    onTextChange = { newE ->
                        list[i] = newE
                        list.removeIf { it.isBlank() }
                        list.add("")
                    },
                    label = "",
                )
            }
        }
    }
}

@Composable
fun ListNoteElemTextField(
    text: String,
    onTextChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false,
    size: Dp? = null,
) {
    var offsetX by remember { mutableStateOf(0f) }
    val maxOffsetX = 500f
    var done by remember { mutableStateOf(false) }

    val newModifier = modifier
        .alpha((maxOffsetX - abs(offsetX)) / maxOffsetX)
        .offset { IntOffset(offsetX.roundToInt(), 0) }
        .pointerInput(text) {
            this.detectHorizontalDragGestures(
                onDragEnd = {
                    if (abs(offsetX) > maxOffsetX) onTextChange("")
                    offsetX = 0f
                },
                onHorizontalDrag = { change, dragAmount ->
                    change.consumeAllChanges()
                    offsetX += dragAmount
                }
            )
        }

    EditorTextField(
        text = text,
        onTextChange = onTextChange,
        label = label,
        modifier = newModifier,
        singleLine = singleLine,
        size = size,
        leadingIcon = {
            IconButton(
                onClick = { done = !done },
                modifier = Modifier.padding(CONST.PADDING)
            ) {
                Icon(if (done) Icons.Sharp.Check else Icons.Sharp.Close, "")
            }
        },
    )
}

@Composable
fun EditorTextField(
    text: String,
    onTextChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false,
    size: Dp? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
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
        ),
        leadingIcon = leadingIcon,
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
