package com.example.guessme.common.base

import android.media.MediaPlayer
import android.provider.MediaStore.Audio.Media
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.example.guessme.R
import com.example.guessme.ui.dialog.NoticeDialog
import java.io.IOException

class BasePlayer(private val fragmentManager: FragmentManager) {
    private var _player: MediaPlayer? = MediaPlayer()
    private val player get() = _player!!


    fun setPlayer(player: MediaPlayer) {
        _player = player
    }

    fun startPlaying(fileName: String?) {

        player.setOnCompletionListener {
            player.apply {
                stop()
                reset()
                release()
                _player = null
            }
        }

        player.apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e("IOException", "prepare() failed")
                val dialog = NoticeDialog(R.string.dialog_record_error)
                dialog.show(fragmentManager, "RecordDialog")
            } catch (e: java.lang.RuntimeException) {
                Log.e("RuntimeException", "runtime exception")
                val dialog = NoticeDialog(R.string.dialog_record_no_file)
                dialog.show(fragmentManager, "RecordDialog")
            }
        }
    }
}