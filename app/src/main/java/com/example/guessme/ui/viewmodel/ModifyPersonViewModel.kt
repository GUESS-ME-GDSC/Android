package com.example.guessme.ui.viewmodel

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guessme.common.base.BasePlayer
import com.example.guessme.common.base.BaseRecorder
import com.example.guessme.data.model.Info
import com.example.guessme.data.response.BaseNullResponseBody
import com.example.guessme.data.response.Data
import com.example.guessme.domain.repository.LocalRepository
import com.example.guessme.domain.repository.ModifyPersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ModifyPersonViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val modifyPersonRepository: ModifyPersonRepository
): ViewModel() {
    private var _imageUri: Uri? = null
    private var _basePlayer: BasePlayer? = null
    val basePlayer get() = _basePlayer!!
    private var _baseRecorder: BaseRecorder? = null
    private val baseRecorder get() = _baseRecorder!!
    private val _recordStatus = MutableLiveData<Boolean>()
    val recordStatus: LiveData<Boolean> get() = _recordStatus
    private var _fileName: String? = null
    val fileName get() = _fileName
    private val _person = MutableLiveData<Data>()
    val person: LiveData<Data> = _person
    private var _infoList = MutableLiveData<List<Info>?>()
    val infoList: LiveData<List<Info>?> = _infoList
    private val _favorite = MutableLiveData<Boolean>()
    val favorite: LiveData<Boolean> = _favorite
    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess: LiveData<Boolean> = _deleteSuccess

    fun setImage(image: Uri) {
        _imageUri = image
    }

    fun setPerson(data: Data) {
        _person.postValue(data)
        _infoList.postValue(data.info)
        _favorite.postValue(data.favorite)

    }

    fun setFavorite(data: Boolean) {
        _favorite.postValue(data)
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

    suspend fun deletePerson(id: Int) {
        try {
            val token = withContext(Dispatchers.IO) {
                localRepository.getToken().first()
            }
            val response: Response<BaseNullResponseBody> = modifyPersonRepository.deletePerson("Bearer $token", id)
            val status = response.body()?.status
            Log.d("status", status.toString())

            if((status == 200) and response.isSuccessful) {
                _deleteSuccess.postValue(true)
            } else {
                _deleteSuccess.postValue(false)
            }
        } catch (e: java.lang.Exception) {
            _deleteSuccess.postValue(false)
        }
    }

}