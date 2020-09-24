package com.example.notes.main

import android.content.*
import android.os.*
import androidx.activity.*
import androidx.activity.result.contract.*
import androidx.appcompat.app.*
import androidx.compose.ui.platform.*
import androidx.lifecycle.*
import com.example.notes.data.*
import com.example.notes.editor.*
import com.example.notes.ui.*

class MainActivity : AppCompatActivity() {

    // all notes
    val notesVM: ConcurrentNotesViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory(this.application)
    }

    // notes tagged for removal
    val taggedVM: TaggedViewModel by viewModels()

    // receiving note
    val launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
    ) {
        it?.data?.extras?.getParcelable<Note>(GLOBAL.note)?.let { note ->
            notesVM.putNoteFromEditor(note)
        }
    }

    // sending note
    fun goToEditor(note: Note? = null) {
        launcher.launch(
            Intent(this, EditorActivity::class.java).apply {
                if (note != null) {
                    putExtra(GLOBAL.note, note)
                }
            }
        )
    }

    // deleting tagged notes
    fun removeTagged() {
        notesVM.removeNotes(taggedVM.tagged)
        taggedVM.clearTagged()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("create")


        setContent {
            NotesTheme {
                MainScreen(
                    notes = notesVM.notes,
                    onAddNote = ::goToEditor,
                    onEditNote = ::goToEditor,
                    tagged = taggedVM.tagged,
                    onTagNote = taggedVM::tagNote,
                    onRemoveTagged = ::removeTagged,
                    onCancelRemoval = taggedVM::clearTagged,
                )
            }
        }
    }

    override fun onStop() {
        notesVM.saveNotes()
        super.onStop()
    }
}


