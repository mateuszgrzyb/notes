package com.example.notes.main

import android.content.Intent
import android.content.pm.PackageManager
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
    private val notesVM by viewModels<ConcurrentNotesViewModel> {
        ViewModelProvider.AndroidViewModelFactory(this.application)
    }

    // notes tagged for removal
    private val taggedVM by viewModels<TaggedViewModel>()

    // receiving note
    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        it?.data?.extras?.getParcelable<Note>(CONST.NOTE)?.let { note ->
            notesVM.putNoteFromEditor(note)
        }
    }

    // sending note
    private fun goToEditor(note: Note) {
        launcher.launch(
            Intent(this, EditorActivity::class.java).putExtra(CONST.NOTE, note)
        )
    }

    // deleting tagged notes
    private fun removeTagged() {
        notesVM.removeNotes(taggedVM.tagged)
        taggedVM.clearTagged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermissions(this)

        setContent {
            NotesTheme {
                MainScreen(
                    notes = notesVM.notes,
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

fun getRequiredPermissions(activity: AppCompatActivity): List<String> =
    activity.packageManager.getPackageInfo(
        activity.packageName,
        PackageManager.GET_PERMISSIONS
    ).requestedPermissions.toList()

fun checkPermission(activity: AppCompatActivity, permission: String) =
    if (
        activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED
    ) activity.requestPermissions(arrayOf(permission), 0)
    else Unit

fun checkPermissions(activity: AppCompatActivity) =
    getRequiredPermissions(activity).forEach { p ->
        checkPermission(activity, p)
    }
