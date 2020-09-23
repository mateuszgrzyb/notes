package com.example.notes.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import java.io.File

open class SavedViewModel<T>(
    val app: Application,
    init: () -> T,
    val serializer: (T) -> String,
    deserializer: (String) -> T
): AndroidViewModel(app) {

    val name = "somestupidassnamefile"

    var savePoint: T = run {

        val file = File(app.filesDir, name)

        if (!file.exists()) file.createNewFile()

        if (file.length() <= 0) init()
        else deserializer(app
            .openFileInput(name)
            .bufferedReader()
            .readLines()
            .joinToString()
        )
    }

    override fun onCleared() {
        app.openFileOutput(name, Context.MODE_PRIVATE).use {
            it.write(
                serializer(savePoint).toByteArray()
            )
        }
        super.onCleared()
    }
}
