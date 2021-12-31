package com.example.notes.media

import android.media.MediaPlayer
import java.io.File

class VoiceMemoPlayer(
    private val file: File,
    private val onCompletionListener: MediaPlayer.() -> Unit,
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

    private var player: Player? = null

    fun start(nanoseconds: Long) {
        player?.release()
        player = Player().apply {
            prepare()
            seekTo(nanoseconds / 1_000_000, MediaPlayer.SEEK_PREVIOUS_SYNC)
        }
    }

    fun stop() {
        player?.release()
        player = null
    }
}
