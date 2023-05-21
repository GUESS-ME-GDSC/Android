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
import com.example.guessme.data.response.BaseNullResponseBody
import com.example.guessme.data.response.Data
import com.example.guessme.domain.repository.LocalRepository
import com.example.guessme.domain.repository.ModifyPersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ModifyPersonViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val modifyPersonRepository: ModifyPersonRepository
): ViewModel() {
    private var _imageUri: Uri? = null
    val imageUri: Uri? get() = _imageUri
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
    private val _favorite = MutableLiveData<Boolean>()
    val favorite: LiveData<Boolean> = _favorite
    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess: LiveData<Boolean> = _deleteSuccess
    private val _modifySuccess = MutableLiveData<Boolean>()
    val modifySuccess: LiveData<Boolean> = _modifySuccess

    fun setImage(image: Uri) {
        _imageUri = image
    }

    fun setPerson(data: Data) {
        _person.postValue(data)
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

    suspend fun modifyPersonFavorite(userId: Int) {
        try {
            val token = withContext(Dispatchers.IO) {
                localRepository.getToken().first()
            }
            val response = modifyPersonRepository.modifyPersonFavorite("Bearer $token", userId)
            val status = response.body()?.status
            Log.d("favoriteStatus", response.toString())

            if ((status != 201) or !response.isSuccessful) {
                _modifySuccess.postValue(false)
            }
        } catch (e: java.lang.Exception) {
            Log.d("modify person favorite viewmodel", e.toString())
            _modifySuccess.postValue(false)
        }
    }

    suspend fun modifyPerson(userId: Int, person: Person, image: File?) {
        try {
            val token = withContext(Dispatchers.IO) {
                localRepository.getToken().first()
            }
            var imageRequestBody: RequestBody?
            var imageMultipartBody: MultipartBody.Part? = null
            image?.let {
                imageRequestBody = image.asRequestBody("image/jpeg".toMediaType())
                imageMultipartBody = MultipartBody.Part.createFormData(name= "image", filename = image.name, body = imageRequestBody!!)
            }

            var voiceFile: File?
            var voiceRequestBody: RequestBody?
            var voiceMultipartBody: MultipartBody.Part? = null
            person.voice?.let {
                voiceFile = File(it.path!!)
                voiceRequestBody = voiceFile!!.asRequestBody("audio/mp4".toMediaType())
                voiceMultipartBody = MultipartBody.Part.createFormData(name= "voice", filename = voiceFile!!.name, body = voiceRequestBody!!)
            }

            val nameRequestBody = person.name.toRequestBody("text/plain".toMediaType())
            val relationRequestBody = person.relation.toRequestBody("text/plain".toMediaType())
            val birthRequestBody = person.birth.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val residenceRequestBody = person.residence.toRequestBody("text/plain".toMediaType())

            val response = modifyPersonRepository.modifyPerson(
                "Bearer $token",
                userId,
                nameRequestBody,
                relationRequestBody,
                birthRequestBody,
                residenceRequestBody,
                imageMultipartBody,
                voiceMultipartBody)
            val status = response.body()?.status
            Log.d("personStatus", response.toString())
            Log.d("personMessage", response.body()!!.message)

            if ((status == 201) and response.isSuccessful) {
                _modifySuccess.postValue(true)
            } else {
                _modifySuccess.postValue(false)
            }
        } catch (e: Exception) {
            Log.d("modify person viewmodel", e.toString())
            _modifySuccess.postValue(false)
        }
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