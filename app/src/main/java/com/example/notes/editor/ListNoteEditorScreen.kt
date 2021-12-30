package com.example.notes.editor

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.notes.data.ListNoteRow
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
            *(note?.body ?: listOf()).toTypedArray(), ListNoteRow()
        )
    }
    val uuid = remember { note?.uuid ?: UUID.randomUUID() }

    BaseNoteEditorScreen(
        title = title,
        changeTitle = changeTitle,
        onNoteChange = {
            val body = list.toMutableList()
            body.removeLast()
            onNoteChange(ListNote(title, body, uuid))
        },
        onActionBack = { onNoteChange(null) },
    ) {
        LazyColumn {
            itemsIndexed(list) { i, e ->
                ListNoteElemTextField(
                    elem = e,
                    onElemChange = { newE ->
                        list[i] = newE
                        list.removeAll { it.text.isBlank() }
                        list.add(ListNoteRow())
                    },
                    enableButton = (i + 1 < list.size)
                )
            }
        }
    }
}

@Composable
fun ListNoteElemTextField(
    elem: ListNoteRow,
    onElemChange: (ListNoteRow) -> Unit,
    modifier: Modifier = Modifier,
    enableButton: Boolean = true,
    size: Dp? = null,
) {
    var offsetX by remember { mutableStateOf(0f) }
    val maxOffsetX = 500f

    val newModifier = modifier
        .alpha((maxOffsetX - abs(offsetX)) / maxOffsetX)
        .offset { IntOffset(offsetX.roundToInt(), 0) }
        .pointerInput(elem.text) {
            this.detectHorizontalDragGestures(
                onDragEnd = {
                    if (abs(offsetX) > maxOffsetX) onElemChange(ListNoteRow())
                    offsetX = 0f
                },
                onHorizontalDrag = { change, dragAmount ->
                    change.consumeAllChanges()
                    offsetX += dragAmount
                }
            )
        }

    EditorTextField(
        text = elem.text,
        onTextChange = { text -> onElemChange(ListNoteRow(elem.mark, text)) },
        label = "",
        modifier = newModifier,
        singleLine = true,
        size = size,
        leadingIcon = {
            IconButton(
                onClick = { onElemChange(ListNoteRow(!elem.mark, elem.text)) },
                modifier = Modifier.padding(CONST.PADDING),
                enabled = enableButton,
            ) {
                Icon(if (elem.mark) Icons.Sharp.Check else Icons.Sharp.Close, "")
            }
        },
    )
}
