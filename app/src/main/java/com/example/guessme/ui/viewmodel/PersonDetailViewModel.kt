package com.example.guessme.ui.viewmodel

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guessme.common.base.BasePlayer
import com.example.guessme.data.model.Info
import com.example.guessme.data.model.InfoList
import com.example.guessme.data.repository.LocalRepositoryImpl
import com.example.guessme.data.response.BaseResponseBody
import com.example.guessme.data.response.Data
import com.example.guessme.data.response.PersonResponse
import com.example.guessme.domain.repository.PersonDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PersonDetailViewModel @Inject constructor(
    private val personDetailRepository: PersonDetailRepository,
    private val localRepositoryImpl: LocalRepositoryImpl
): ViewModel() {
    private var _person = MutableLiveData<Data>()
    val person get() = _person
    private var _player: BasePlayer? = null
    private val player get() = _player!!
    private var _infoList = MutableLiveData<List<Info>?>()
    val infoList: LiveData<List<Info>?> = _infoList
    private var _isDelete = MutableLiveData<Boolean>()
    val isDelete: LiveData<Boolean> = _isDelete
    private var _getPersonSuccess = MutableLiveData<Boolean>()
    private val _addSuccess = MutableLiveData<Boolean>()
    val addSuccess: LiveData<Boolean> = _addSuccess
    val getPersonSuccess: LiveData<Boolean> = _getPersonSuccess

    fun setAddSuccess(data: Boolean) {
        _addSuccess.postValue(data)
    }

    fun addInfoList(info: Info) {
        var list: ArrayList<Info>? = null
        if (infoList.value == null){
            list = ArrayList<Info>()
        } else {
            list = infoList.value as ArrayList<Info>?
            list!!.add(info)
        }
        list!!.add(info)

        _infoList.postValue(list)
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

    suspend fun getPerson(id: Int) {
        try {
            val token = withContext(Dispatchers.IO) {
                localRepositoryImpl.getToken().first()
            }
            //owner 추후 수정해야 함
            val response: Response<PersonResponse> = personDetailRepository.getPerson("Bearer $token", 2)
            val status = response.body()?.status

            if((status == 200) and response.isSuccessful) {
                val person = response.body()?.data
                Log.d("person", person.toString())
                _person.postValue(person!!)
                _infoList.postValue(person.info!!)
            }else {
                _getPersonSuccess.postValue(false)
            }
        } catch (e: java.lang.Exception) {
            Log.d("e", e.toString())
            _getPersonSuccess.postValue(false)
        }
    }

    suspend fun addInfo(info: Info, id: Int) {
        try {
            val token = withContext(Dispatchers.IO) {
                localRepositoryImpl.getToken().first()
            }
            val temp = ArrayList<Info>()
            temp.add(info)

            val response: Response<BaseResponseBody> = personDetailRepository.addInfo(token, id, InfoList(temp))
            val status = response.body()?.status
            Log.d("status", status.toString())
            Log.d("status", response.body()!!.data)

            if ((status == 201) and response.isSuccessful) {
                _addSuccess.postValue(true)
            }else {
                _addSuccess.postValue(false)
            }
        }catch (e: Exception) {
            Log.e("e", e.toString())
            _addSuccess.postValue(false)
        }
    }

}