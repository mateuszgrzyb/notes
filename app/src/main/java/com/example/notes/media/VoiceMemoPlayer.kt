package com.example.notes.media

import android.media.MediaPlayer
import android.os.CountDownTimer
import java.io.File

class VoiceMemoPlayer(
    private val file: File,
    private val nanosInFuture: Long,
    private val onCompletionListener: MediaPlayer.() -> Unit,
    private val onTickListener: (Long) -> Unit,
) {

    inner class Player : MediaPlayer() {
        init {
            setDataSource(file.path)
            setOnSeekCompleteListener {
                start()
            }
            setOnCompletionListener(onCompletionListener)
        }
    }

    inner class Timer : CountDownTimer(nanosInFuture / 1000, 10) {
        override fun onTick(millisUnfinished: Long) = onTickListener(millisUnfinished)
        override fun onFinish() = Unit
    }

    private var player: Player? = null
    private var timer: Timer? = null

    fun start(nanoseconds: Long) {
        timer = Timer().apply {
            start()
        }
        player?.release()
        player = Player().apply {
            prepare()
            seekTo(nanoseconds / 1_000_000, MediaPlayer.SEEK_PREVIOUS_SYNC)
        }
    }

    fun stop() {
        player?.release()
        player = null
        timer?.cancel()
        timer = null
    }
}
