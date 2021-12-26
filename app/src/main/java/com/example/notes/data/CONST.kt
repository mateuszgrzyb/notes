package com.example.notes.data

import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.Json

object CONST {
    const val TITLE = "title"
    const val BODY = "body"
    const val UUID = "uuid"
    const val NOTE = "note"
    val PADDING = 8.dp
    val JSON = Json { encodeDefaults = true }
}
