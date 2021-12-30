package com.example.notes.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.notes.data.CONST
import com.example.notes.data.VoiceNote
import com.example.notes.media.VoiceMemoPlayer
import com.example.notes.media.VoiceMemoRecorder
import java.io.File
import java.util.UUID

enum class PlaybackState {
    PLAYING, RECORDING, IDLE
}

@Composable
fun VoiceSlider(
    current: Long,
    setCurrent: (Long) -> Unit,
    value: Float,
    setValue: (Float) -> Unit,
    max: Long
) {
    Column {
        Row {
            Text("0.00")
            Spacer(Modifier.size(10.dp))
            Text("%.2f".format(max * value))
            Spacer(Modifier.size(10.dp))
            Text("$current")
            Spacer(Modifier.size(10.dp))
            Text("%.2f".format(max.toFloat()))
        }
        Slider(
            value = value,
            onValueChange = setValue,
            onValueChangeFinished = { setCurrent((max.toDouble() * value * 1_000_000_000).toLong()) }
        )
    }
}

@Composable
fun VoiceNoteEditorScreen(
    note: VoiceNote?,
    onNoteChange: (VoiceNote?) -> Unit,
) {

    var title by remember { mutableStateOf(note?.title ?: "") }
    var body by remember { mutableStateOf(note?.body) }
    val uuid = remember { note?.uuid ?: UUID.randomUUID() }

    var state by remember { mutableStateOf(PlaybackState.IDLE) }
    val context = LocalContext.current
    val file = File(context.filesDir, uuid.toString())

    var time by remember { mutableStateOf<Long?>(null) }
    var currentTime by remember { mutableStateOf(0L) }
    var sliderValue by remember { mutableStateOf(0f) }

    val recorder = VoiceMemoRecorder(file)
    val player = VoiceMemoPlayer(
        file = file,
        nanosInFuture = currentTime,
        onCompletionListener = {
            state = PlaybackState.IDLE
            currentTime = 0L
            sliderValue = 0f
        },
    ) {
        if (time != null) {
            val c = time!! - it * 1000
            currentTime = c
            sliderValue = c.toFloat() / time!!
        }
    }

    val playEnabled = (state != PlaybackState.RECORDING && body != null)
    fun playOnClick() {
        state = PlaybackState.PLAYING
        player.start(currentTime)
    }

    val recordEnabled = (state != PlaybackState.PLAYING && body == null)
    fun recordOnClick() {
        state = PlaybackState.RECORDING
        recorder.start()
    }

    val stopEnabled = (state != PlaybackState.IDLE)
    fun stopOnClick() {
        when (state) {
            PlaybackState.PLAYING -> player.stop()
            PlaybackState.RECORDING -> {
                time = recorder.stop()
                body = file
            }
            else -> Unit
        }
        state = PlaybackState.IDLE
        currentTime = 0L
        sliderValue = 0f
    }

    val deleteEnabled = (state == PlaybackState.IDLE && body != null)
    fun deleteOnClick() {
        body = null
        time = null
    }

    BaseNoteEditorScreen(
        title = title,
        changeTitle = { title = it },
        onNoteChange = { onNoteChange(VoiceNote(title, body, uuid)) },
        onActionBack = { onNoteChange(null) },
    ) {
        Column {

            if (time != null) {
                VoiceSlider(
                    current = currentTime,
                    setCurrent = { currentTime = it },
                    value = sliderValue,
                    setValue = { sliderValue = it },
                    max = (time!!.toDouble() / 1_000_000_000).toLong()
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(CONST.PADDING * 3),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                Button(
                    enabled = playEnabled,
                    onClick = ::playOnClick,
                ) {
                    Text("Play")
                }
                Button(
                    enabled = recordEnabled,
                    onClick = ::recordOnClick,
                ) {
                    Text("Record")
                }
                Button(
                    enabled = stopEnabled,
                    onClick = ::stopOnClick,
                ) {
                    Text("Stop")
                }
                Button(
                    enabled = deleteEnabled,
                    onClick = ::deleteOnClick,
                ) {
                    Text("Delete")
                }
            }
        }
    }
}
