package com.example.guessme.ui.viewmodel

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import com.example.guessme.common.base.BasePlayer
import com.example.guessme.data.model.Person

class PersonDetailViewModel: ViewModel() {
    private var _person: Person? = null
    val person get() = _person!!
    private var _player: BasePlayer? = null
    private val player get() = _player!!

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

}