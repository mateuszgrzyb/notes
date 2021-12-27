package com.example.notes.editor

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.notes.data.CONST
import com.example.notes.data.ListNote
import com.example.notes.data.Note
import com.example.notes.data.TextNote
import com.example.notes.ui.NotesTheme

class EditorActivity : AppCompatActivity() {

    private fun goBack(note: Note? = null) {
        if (!(note == null || note.isEmpty())) {
            intent.putExtra(CONST.NOTE, note)
            // the most important thing when passing data
            // to main activity through intents
            setResult(RESULT_OK, intent)
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
                        TextNoteEditorScreen(note = note as TextNote?) { note -> goBack(note) }
                    else ->
                        ListNoteEditorScreen(note = note as ListNote?) { note -> goBack(note) }
                }
            }
        }
    }
}
