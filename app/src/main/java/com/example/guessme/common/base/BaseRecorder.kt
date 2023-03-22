package com.example.guessme.common.base

import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.IOException

class BaseRecorder {
    private var _recorder: MediaRecorder? = null
    private val recorder get() = _recorder!!

    fun setRecorder(recorder: MediaRecorder) {
        _recorder = recorder
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun startRecording(fileName: String) {

        recorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioSamplingRate(48000)
            setAudioEncodingBitRate(128000)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e("IOException", "prepare() failed")
            }

            start()
        }

    }

    fun stopRecording() {
        recorder.apply {
            stop()
            reset()
            release()
        }
        _recorder = null
    }
}