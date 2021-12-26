package com.example.notes.db

import android.app.Application
import com.example.notes.data.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DB(app: Application) {
    private val db = AppDatabase.getInstance(app)
    private val noteDao = db.noteDao()

    suspend fun loadNotes(): List<Note> = withContext(Dispatchers.IO) {
        noteDao.getAll()
    }
    suspend fun saveNotes(notes: List<Note>) = withContext(Dispatchers.IO) {
        noteDao.insertAll(notes)
    }
}
