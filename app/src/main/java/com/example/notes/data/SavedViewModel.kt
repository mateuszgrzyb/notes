package com.example.notes.data

import android.app.*
import android.content.*
import androidx.lifecycle.*
import java.io.*

open class SavedViewModel<T>(
    val app: Application,
    val init: () -> T,
    val serializer: (T) -> String,
    val deserializer: (String) -> T
): AndroidViewModel(app) {

    val name = "somestupidassnamefile"

    fun load(): T {

        val file = File(app.filesDir, name)

        if (!file.exists()) file.createNewFile()

        return if (file.length() <= 0) init()
        else deserializer(app
            .openFileInput(name)
            .bufferedReader()
            .readLines()
            .joinToString()
        )
    }

    fun save(state: T) {
        app.openFileOutput(name, Context.MODE_PRIVATE).use {
            it.write(
                serializer(state).toByteArray()
            )
        }
    }
}
