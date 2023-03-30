package com.example.guessme.ui.viewmodel

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guessme.common.base.BasePlayer
import com.example.guessme.data.model.Info
import com.example.guessme.data.response.Data
import com.example.guessme.data.response.PersonResponse
import com.example.guessme.domain.repository.LocalRepository
import com.example.guessme.domain.repository.ScoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ScoreViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val scoreRepository: ScoreRepository
): ViewModel() {
    private val _person = MutableLiveData<Data>()
    val person: LiveData<Data> = _person
    private val _getPerson = MutableLiveData<Boolean>()
    val getPerson: LiveData<Boolean> = _getPerson
    private val _infoList = MutableLiveData<List<Info>?>()
    val infoList: LiveData<List<Info>?> = _infoList
    private var _player: BasePlayer? = null
    private val player get() = _player!!

    fun setPlayer(player: BasePlayer) {
        _player = player
        player.setPlayer(MediaPlayer())
    }

    fun startPlaying(name: String?) {
        player.startPlaying(name)
    }
    suspend fun getPerson(id: Int) {
        try {
            val token = withContext(Dispatchers.IO) {
                localRepository.getToken().first()
            }
            val response: Response<PersonResponse> = scoreRepository.getPerson("Bearer $token",id)
            val status = response.body()?.status

            if((status == 200) and response.isSuccessful) {
                val person = response.body()?.data

                _person.postValue(person!!)
                person.info?.let {
                    _infoList.postValue(person.info)
                }
            } else {
                _getPerson.postValue(false)
            }
        } catch (e: java.lang.Exception) {
            _getPerson.postValue(false)
        }
    }
}