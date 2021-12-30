package com.example.notes.editor

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.notes.data.CONST
import com.example.notes.data.ListNote
import com.example.notes.data.Note
import com.example.notes.data.TextNote
import com.example.notes.data.VoiceNote
import com.example.notes.ui.NotesTheme

class EditorActivity : AppCompatActivity() {

    private fun goBack(note: Note? = null) {
        if (!(note == null || note.isEmpty())) {
            setResult(RESULT_OK, intent.putExtra(CONST.NOTE, note))
        }
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val note: Note? = intent.extras?.getParcelable(CONST.NOTE)

        setContent {
            NotesTheme {
                when (note) {
                    is TextNote ->
                        TextNoteEditorScreen(note) { note -> goBack(note) }
                    is ListNote ->
                        ListNoteEditorScreen(note) { note -> goBack(note) }
                    is VoiceNote ->
                        VoiceNoteEditorScreen(note) { note -> goBack(note) }
                }
            }
        }
    }
}
