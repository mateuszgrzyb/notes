package com.example.notes.editor

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import com.example.notes.data.CONST
import com.example.notes.data.ListNote
import java.util.UUID
import kotlin.math.abs
import kotlin.math.roundToInt

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
    val uuid = remember { note?.uuid ?: UUID.randomUUID() }

    BaseNoteEditorScreen(
        title = title,
        changeTitle = changeTitle,
        onNoteChange = {
            list.removeLast()
            onNoteChange(ListNote(title, list, uuid))
        },
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
