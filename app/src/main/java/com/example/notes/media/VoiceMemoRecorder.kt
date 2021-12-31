package com.example.notes.media

import android.media.MediaRecorder
import java.io.File

class VoiceMemoRecorder(
    private val file: File,
) {

    inner class Recorder : MediaRecorder() {
        init {
            setAudioSource(AudioSource.MIC)
            setOutputFormat(OutputFormat.THREE_GPP)
            setOutputFile(file.path)
            setAudioEncoder(AudioEncoder.AMR_NB)
        }
    }

    private var recorder: Recorder? = null
    private var start: Long? = null

    fun start() {
        recorder = Recorder().apply {
            prepare()
            start()
        }
        start = System.nanoTime()
    }

    fun stop(): Long {
        val stop = System.nanoTime()
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        return start?.let { stop - it } ?: 0L
    }
}
