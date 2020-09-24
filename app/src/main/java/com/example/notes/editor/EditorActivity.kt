package com.example.notes.editor

import android.os.*
import androidx.appcompat.app.*
import androidx.compose.ui.platform.*
import com.example.notes.data.*
import com.example.notes.ui.*

class EditorActivity : AppCompatActivity() {


    fun sendNote(note: Note) {
        intent.putExtra(GLOBAL.note, note)
        // the most important thing when passing data
        // to main activity through intents
        setResult(RESULT_OK, intent)
    }

    fun goBack(note: Note? = null) {
        if (!(note == null || note.isEmpty())) sendNote(note)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val note: Note? = intent.extras?.getParcelable(GLOBAL.note)

        setContent {
            NotesTheme {
                EditorScreen(
                    note = note,
                    onNoteChange = ::goBack,
                    onActionBack = ::goBack
                )
            }
        }
    }
}



