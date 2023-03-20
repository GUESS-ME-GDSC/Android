package com.example.guessme.ui.viewmodel

import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class AddPersonViewModel: ViewModel() {
    private var _imageUri: Uri? = null
    val imageUri get() = _imageUri!!
    val audioUri get() = Uri.fromFile(File(fileName))
    private var _player: MediaPlayer? = null
    val player get() = _player!!
    private var _recorder: MediaRecorder? = null
    val recorder get() = _recorder!!
    private var _recordStatus = MutableLiveData<Boolean>()
    val recordStatus: LiveData<Boolean> get() = _recordStatus!!
    private var _fileName: String? = null
    val fileName get() = _fileName!!

    fun setImage(image: Uri) {
        _imageUri = image
    }

    fun setPlayer(player: MediaPlayer?) {
        _player = player
    }

    fun setRecorder(recorder: MediaRecorder?) {
        _recorder = recorder
    }

    fun setRecordStatus(status: Boolean) {
        _recordStatus.postValue(status)
    }

    fun setFileName(name: String) {
        _fileName = name
    }
}