package com.example.notes.editor

import android.os.CountDownTimer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
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
fun FloatText(value: Float) {
    Text(
        text = "%.2f".format(value),
        fontWeight = FontWeight.Bold,
    )
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

        Spacer(Modifier.padding(CONST.PADDING))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            FloatText(0.00f)
            FloatText(max.toFloat() * value)
            FloatText(max.toFloat())
        }

        Slider(
            value = value,
            onValueChange = setValue,
            onValueChangeFinished = { setCurrent((max.toDouble() * value * 1_000_000_000).toLong()) },
            modifier = Modifier.padding(horizontal = 20.dp)
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
    var time by remember { mutableStateOf(note?.time ?: 0L) }
    val uuid = remember { note?.uuid ?: UUID.randomUUID() }

    var state by remember { mutableStateOf(PlaybackState.IDLE) }
    val context = LocalContext.current
    val file = File(context.filesDir, uuid.toString())

    var currentTime by remember { mutableStateOf(0L) }
    var sliderValue by remember { mutableStateOf(0f) }

    class Timer(
        val current: Long,
        val slider: Float,
        max: Long,
        step: Long
    ) : CountDownTimer((max - current) / 1_000_000, step) {
        init {
            start()
        }
        private val millisInFuture = (max - current) / 1_000_000
        override fun onTick(millisUnfinished: Long) {
            val c = millisInFuture - millisUnfinished
            currentTime = c * 1_000_000 + current
            sliderValue = c.toFloat() / millisInFuture + slider
        }
        override fun onFinish() = Unit
    }
    var timer: Timer? = null

    fun reset() {
        state = PlaybackState.IDLE
        currentTime = 0L
        sliderValue = 0f
        timer?.cancel()
        timer = null
    }

    val recorder = VoiceMemoRecorder(file)
    val player = VoiceMemoPlayer(
        file = file,
    ) {
        reset()
    }

    val playEnabled = (state != PlaybackState.RECORDING && body != null)
    fun playOnClick() {
        state = PlaybackState.PLAYING
        player.start(currentTime)
        timer = Timer(currentTime, sliderValue, time, 10)
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
        reset()
    }

    val deleteEnabled = (state == PlaybackState.IDLE && body != null)
    fun deleteOnClick() {
        body = null
        time = 0L
        timer = null
    }

    BaseNoteEditorScreen(
        title = title,
        changeTitle = { title = it },
        onNoteChange = { onNoteChange(VoiceNote(title, body, time, uuid)) },
        onActionBack = { onNoteChange(null) },
    ) {
        Column {

            if (body != null) {
                VoiceSlider(
                    current = currentTime,
                    setCurrent = { currentTime = it },
                    value = sliderValue,
                    setValue = { sliderValue = it },
                    max = (time.toDouble() / 1_000_000_000).toLong()
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(CONST.PADDING * 3),
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
