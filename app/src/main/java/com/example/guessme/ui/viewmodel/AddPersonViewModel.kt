package com.example.guessme.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel

class AddPersonViewModel: ViewModel() {
    private var _imageUri: Uri? = null
    private val imageUri get() = _imageUri!!
    private var _audioUri: Uri? = null
    private val audioUri get() = _audioUri!!

    fun setImage(image: Uri) {
        _imageUri = image
    }

    fun getImage() = imageUri

    fun setAudio(audio: Uri) {
        _audioUri = audio
    }

    fun getAudio() = audioUri
}