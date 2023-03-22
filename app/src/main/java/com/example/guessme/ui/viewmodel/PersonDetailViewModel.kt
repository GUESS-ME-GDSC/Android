package com.example.guessme.ui.viewmodel

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guessme.common.base.BasePlayer
import com.example.guessme.data.model.Info
import com.example.guessme.data.model.Person

class PersonDetailViewModel: ViewModel() {
    private var _person: Person? = null
    val person get() = _person!!
    private var _player: BasePlayer? = null
    private val player get() = _player!!
    private var _infoList = MutableLiveData<ArrayList<Info>>()
    val infoList: LiveData<ArrayList<Info>> = _infoList
    private var _isDelete = MutableLiveData<Boolean>()
    val isDelete: LiveData<Boolean> = _isDelete

    fun setPerson(data: Person?) {
        _person = data
    }

    fun setPlayer(player: BasePlayer) {
        _player = player
        player.setPlayer(MediaPlayer())
    }

    fun startPlaying(name: String?) {
        player.startPlaying(name)
    }

    fun setDelete(value: Boolean) {
        _isDelete.postValue(value)
    }

    fun getInfoList() {
        val data = ArrayList<Info>()

        data.add(Info(0, 1, "취미", "운동"))
        data.add(Info(1, 1, "나이", "24"))

        _infoList.postValue(data)
    }

}