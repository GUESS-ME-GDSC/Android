package com.example.guessme.ui.viewmodel

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guessme.common.base.BasePlayer
import com.example.guessme.common.base.BaseRecorder
import com.example.guessme.data.model.InfoList
import com.example.guessme.data.model.Person

class ModifyPersonViewModel: ViewModel() {
    private var _imageUri: Uri? = null
    private var _basePlayer: BasePlayer? = null
    private val basePlayer get() = _basePlayer!!
    private var _baseRecorder: BaseRecorder? = null
    private val baseRecorder get() = _baseRecorder!!
    private var _recordStatus = MutableLiveData<Boolean>()
    val recordStatus: LiveData<Boolean> get() = _recordStatus
    private var _fileName: String? = null
    val fileName get() = _fileName
    private var _person: Person? = null
    val person get() = _person!!
    private var _infoList = MutableLiveData<InfoList?>()
    val infoList: LiveData<InfoList?> = _infoList

    fun setImage(image: Uri) {
        _imageUri = image
    }

    fun setPerson(data: Person) {
        _person = data
    }

    fun setInfoList(data: InfoList?) {
        _infoList.postValue(data)
    }

    fun setPlayer(player: BasePlayer) {
        _basePlayer = player
        basePlayer.setPlayer(MediaPlayer())
    }

    fun startPlaying(name: String?) {
        basePlayer.startPlaying(name)
    }

    fun setRecorder(data: BaseRecorder?, context: Context) {
        _baseRecorder = data

        val recorder: MediaRecorder = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }

        baseRecorder.setRecorder(recorder)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun startRecording(name: String) {
        _recordStatus.postValue(true)
        baseRecorder.startRecording(name)
    }

    fun stopRecording() {
        baseRecorder.stopRecording()
        _recordStatus.postValue(false)
    }

    fun setFileName(name: String) {
        _fileName = name
    }
}