package com.example.notes.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.lifecycle.ViewModelProvider
import com.example.notes.data.CONST
import com.example.notes.data.ConcurrentNotesViewModel
import com.example.notes.data.Note
import com.example.notes.data.TaggedViewModel
import com.example.notes.editor.EditorActivity
import com.example.notes.ui.NotesTheme

@ExperimentalFoundationApi
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
        it?.data?.extras?.getParcelable<Note>(CONST.NOTE)?.let { note ->
            notesVM.putNoteFromEditor(note)
        }
    }

    // sending note
    fun goToEditor(note: Note? = null) {
        launcher.launch(
            Intent(this, EditorActivity::class.java).apply {
                if (note != null) {
                    putExtra(CONST.NOTE, note)
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
        notesVM.save()
        super.onStop()
    }
}
