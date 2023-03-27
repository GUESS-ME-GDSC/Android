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
import com.example.guessme.data.model.Person
import com.example.guessme.data.response.BaseResponseBody
import com.example.guessme.domain.repository.AddPersonRepository
import com.example.guessme.domain.repository.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.File
import javax.inject.Inject
import java.lang.Exception

@HiltViewModel
class AddPersonViewModel @Inject constructor(
    private val addPersonRepository: AddPersonRepository,
    private val localRepository: LocalRepository
): ViewModel() {
    private var _imageUri: Uri? = null
    val imageUri: Uri? get() = _imageUri
    private var _basePlayer: BasePlayer? = null
    private val basePlayer get() = _basePlayer!!
    private var _baseRecorder: BaseRecorder? = null
    private val baseRecorder get() = _baseRecorder!!
    private var _recordStatus = MutableLiveData<Boolean>()
    val recordStatus: LiveData<Boolean> get() = _recordStatus
    private var _fileName: String? = null
    val fileName get() = _fileName
    private val _addSuccess = MutableLiveData(false)
    val addSuccess: LiveData<Boolean> = _addSuccess
    private val _errorState = MutableLiveData<Int>()
    val errorState: LiveData<Int> = _errorState

    fun setImage(image: Uri) {
        _imageUri = image
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

    suspend fun addPerson(person: Person, image: File?) {
        try {
            val token = withContext(Dispatchers.IO){
                localRepository.getToken().first()
            }
            val response: Response<BaseResponseBody> = addPersonRepository.addPerson(token, person, image)
            val status = response.body()?.status
            Log.d("status", status.toString())
            Log.d("status", response.body()!!.data)

            if((status == 200) and response.isSuccessful) {
                _addSuccess.postValue(true)
            }else if (status == 401) {
                _errorState.postValue(401)
            } else {
                _errorState.postValue(status!!)
            }
        }catch (e: Exception) {
            Log.e("e", e.toString())
            _errorState.postValue(1)
        }
    }
}