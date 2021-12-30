package com.example.notes.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.notes.data.CONST
import com.example.notes.data.ListNote
import com.example.notes.data.Note
import com.example.notes.data.TextNote
import com.example.notes.data.VoiceNote
import com.example.notes.ui.typography

@ExperimentalFoundationApi
@Composable
fun MainScreen(
    notes: List<Note>,
    onEditNote: (Note) -> Unit,
    tagged: List<Note>,
    onTagNote: (Note) -> Unit,
    onRemoveTagged: () -> Unit,
    onCancelRemoval: () -> Unit,
) {
    val deleting = tagged.isNotEmpty()

    Scaffold(
        floatingActionButton = { if (!deleting) NotesFAB(onEditNote) },
        topBar = { if (deleting) NotesTopAppBar(tagged, onRemoveTagged, onCancelRemoval) }
    ) {
        NotesList(
            notes = notes,
            onEditNote = onEditNote,
            tagged = tagged,
            onTagNote = onTagNote,
        )
    }
}

@Composable
fun NotesTopAppBar(
    tagged: List<Note>,
    onRemoveTagged: () -> Unit,
    onCancelRemoval: () -> Unit,
) = TopAppBar(
    title = { Text("Deleting ${tagged.size} notes") },
    actions = {
        IconButton(
            onClick = onRemoveTagged,
        ) {
            Icon(Icons.Default.Delete, "")
        }
        IconButton(
            onClick = onCancelRemoval,
        ) {
            Icon(Icons.Default.Close, "")
        }
    }
)

@Composable
fun MiniFAB(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(40.dp)
    ) {
        Text(
            text = text,
            style = typography.button
        )
    }
}

@Composable
fun NotesFAB(
    onEditNote: (Note) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ConstraintLayout {
        fun miniFABModifier(
            from: ConstrainedLayoutReference,
            to: ConstrainedLayoutReference,
        ): Modifier = Modifier.constrainAs(from) {
            bottom.linkTo(to.top, 16.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        val (fab, voiceButton, listButton, textButton) = createRefs()

        if (expanded) {
            MiniFAB(
                text = "Text",
                modifier = miniFABModifier(textButton, listButton),
            ) {
                expanded = false
                onEditNote(TextNote())
            }

            MiniFAB(
                text = "List",
                modifier = miniFABModifier(listButton, voiceButton),
            ) {
                expanded = false
                onEditNote(ListNote())
            }

            MiniFAB(
                text = "Voice",
                modifier = miniFABModifier(voiceButton, fab),
            ) {
                expanded = false
                onEditNote(VoiceNote())
            }
        }

        FloatingActionButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.constrainAs(fab) { bottom.linkTo(parent.bottom) }
        ) {
            Icon(Icons.Default.Add, "")
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun NotesList(
    notes: List<Note>,
    onEditNote: (Note) -> Unit,
    tagged: List<Note>,
    onTagNote: (Note) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(CONST.PADDING),
    ) {
        BoxWithConstraints {
            val size = (maxWidth - CONST.PADDING * 2) / 2

            Row {
                LazyVerticalGrid(
                    cells = GridCells.Fixed(2),
                    content = {
                        itemsIndexed(notes) {
                            i, note ->
                            NoteItem(
                                note = note,
                                onEditNote = { onEditNote(notes[i]) },
                                tagged = note in tagged,
                                tagging = tagged.isNotEmpty(),
                                onTagNote = { onTagNote(note) },
                                size = size,
                            )
                        }
                    }
                )
            }
        }
    }
}
