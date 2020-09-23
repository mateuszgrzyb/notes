package com.example.notes.editor

import android.os.*
import androidx.appcompat.app.*
import androidx.compose.ui.platform.*
import com.example.notes.data.*

class EditorActivity : AppCompatActivity() {


    fun sendNote(note: Note) {
        println("sending note")
        println(note)
        intent.putExtra("note", note)
        // the most important thing when passing data
        // to main activity through intents
        setResult(RESULT_OK, intent)
    }

    fun goBack() = finish()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val note: Note? = intent.extras?.getParcelable<Note>("note")?.also {
            println("receiving note")
            println(it)
        }

        setContent {
            EditorScreen(
                note = note,
                onNoteChange = { if (!it.isEmpty()) sendNote(it); goBack() },
                onActionBack = ::goBack
            )
        }
    }
}



