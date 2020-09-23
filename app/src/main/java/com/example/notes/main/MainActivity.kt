package com.example.notes.main

import android.content.*
import android.os.*
import androidx.activity.*
import androidx.activity.result.contract.*
import androidx.appcompat.app.*
import androidx.compose.material.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.lifecycle.*
import com.example.notes.data.*
import com.example.notes.editor.*
import com.example.notes.ui.*

class MainActivity : AppCompatActivity() {

    // receiving note
    val launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
    ) {
        it?.data?.extras?.getParcelable<Note>("note")?.let { note ->
            notesVM.putNoteFromEditor(note)
        }

        notesVM.currentlyEdited = null
    }

    // sending note
    fun goToEditor(note: Note?) {
        launcher.launch(
            Intent(this, EditorActivity::class.java).apply {
                if (note != null) {
                    println("sending note from main")
                    println(note)
                    putExtra("note", note)
                }
            }
        )
    }

    val notesVM: NotesViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory(this.application)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notesVM.currentlyEdited = null

        setContent {
            NotesTheme {
                // A surface container using the 'background' color from the theme
                //Surface(color = MaterialTheme.colors.background) {
                Surface(color = Color.Gray) {
                    MainScreen(
                        notes = notesVM.notes,
                        onAddNote = { goToEditor(null) },
                        onRemoveNote = notesVM::removeNote,
                        onEditNote = { goToEditor(notesVM.currentlyEdited) },
                        currentlyEdited = notesVM.currentlyEdited,
                        onChangeCurrentlyEdited = notesVM::changeCurrentlyEdited
                    )
                }
            }
        }
    }
}


