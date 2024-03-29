package com.example.notes.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import java.io.File

abstract class SavedViewModel<T>(
    val app: Application,
) : AndroidViewModel(app) {

    val name = "notes.save.somewhere"

    fun load(): T {

        val file = File(app.filesDir, name)

        if (!file.exists()) file.createNewFile()

        return if (file.length() <= 0) init()
        else deserialize(
            app
                .openFileInput(name)
                .bufferedReader()
                .readLines()
                .joinToString()
        )
    }

    fun save(state: T) {
        app.openFileOutput(name, Context.MODE_PRIVATE).use {
            it.write(
                serialize(state).toByteArray()
            )
        }
    }

    abstract fun deserialize(data: String): T
    abstract fun serialize(state: T): String
    abstract fun init(): T
}
