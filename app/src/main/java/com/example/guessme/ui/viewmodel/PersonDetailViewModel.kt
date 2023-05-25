package com.example.guessme.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guessme.common.base.BasePlayer
import com.example.guessme.data.model.IdList
import com.example.guessme.data.model.Info
import com.example.guessme.data.model.InfoList
import com.example.guessme.data.response.BaseNullResponseBody
import com.example.guessme.data.response.Data
import com.example.guessme.data.response.PersonResponse
import com.example.guessme.domain.repository.LocalRepository
import com.example.guessme.domain.repository.PersonDetailRepository
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.addAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PersonDetailViewModel @Inject constructor(
    private val personDetailRepository: PersonDetailRepository,
    private val localRepository: LocalRepository
): ViewModel() {
    private var _person = MutableLiveData<Data>()
    val person get() = _person
    private var _basePlayer: BasePlayer? = null
    private val basePlayer get() = _basePlayer!!
    private var _infoList = MutableLiveData<List<Info>?>()
    val infoList: LiveData<List<Info>?> = _infoList
    private var _isDelete = MutableLiveData<Boolean>()
    val isDelete: LiveData<Boolean> = _isDelete
    private var _getPersonSuccess = MutableLiveData<Boolean>()
    private val _addSuccess = MutableLiveData<Boolean>()
    val addSuccess: LiveData<Boolean> = _addSuccess
    val getPersonSuccess: LiveData<Boolean> = _getPersonSuccess
    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess: LiveData<Boolean> = _deleteSuccess
    private val _modifySuccess = MutableLiveData<Boolean>()
    val modifySuccess: LiveData<Boolean> = _deleteSuccess


    fun setAddSuccess(data: Boolean) {
        _addSuccess.postValue(data)
    }

    private fun addInfoList(info: Info) {
        var list = infoList.value

        list = if (list == null){
            ArrayList()
        } else {
            list as ArrayList<Info>
        }
        list.add(info)

        _infoList.postValue(list)
    }

    fun setPlayer(base: BasePlayer) {
        _basePlayer = base
    }

    fun startPlaying(name: String?) {
        basePlayer.startPlaying(name)
    }

    fun setDelete(value: Boolean) {
        _isDelete.postValue(value)
    }

    suspend fun getPerson(id: Int) {
        try {
            val token = withContext(Dispatchers.IO) {
                localRepository.getToken().first()
            }

            val response: Response<PersonResponse> = personDetailRepository.getPerson("Bearer $token", id)
            val status = response.body()?.status
            Log.d("person", response.body()!!.data!!.toString())

            if((status == 200) and response.isSuccessful) {
                val person = response.body()?.data

                _person.postValue(person!!)
                person.info?.let {
                    _infoList.postValue(person.info)
                }
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
                localRepository.getToken().first()
            }
            val infoList = List(1){info}

            val response: Response<BaseNullResponseBody> = personDetailRepository.addInfo("Bearer $token",id,
                InfoList(infoList))
            val status = response.body()?.status
            Log.d("status", status.toString())

            if ((status == 201) and response.isSuccessful) {
//                addInfoList(info)
                _addSuccess.postValue(true)
            }else {
                _addSuccess.postValue(false)
            }
        }catch (e: Exception) {
            Log.e("e", e.toString())
            _addSuccess.postValue(false)
        }
    }

    suspend fun deleteInfo(idList: IdList) {
        try {
            val token = withContext(Dispatchers.IO) {
                localRepository.getToken().first()
            }
            Log.d("idList", idList.toString())
            val response: Response<BaseNullResponseBody> = personDetailRepository.deleteInfo("Bearer $token", idList)
            val status = response.body()?.status
            Log.d("status", status.toString())
            Log.d("message", response.body()!!.message)

            if ((status == 200) and response.isSuccessful) {
                _deleteSuccess.postValue(true)
            } else {
                _deleteSuccess.postValue(false)
            }
        } catch (e: java.lang.Exception) {
            Log.d("e", e.toString())
            _deleteSuccess.postValue(false)
        }
    }

    suspend fun modifyInfo(info: Info, userId: Int) {
        try {
            val token = withContext(Dispatchers.IO) {
                localRepository.getToken().first()
            }

            val infoRequest = createRequestBodyFromInfoList(info)
            val imageMultipartBody = MultipartBody.Part.createFormData(name= "info", filename = info.infoKey, body = listOf(infoRequest))
            val response = personDetailRepository.modifyInfo("Bearer $token", imageMultipartBody, userId)
            val status = response.body()?.status
            Log.d("status", status.toString())
            Log.d("message", response.body()!!.data.toString())

            if ((status == 200) and response.isSuccessful) {
                _modifySuccess.postValue(true)
            } else {
                _modifySuccess.postValue(false)
            }
        } catch (e: Exception) {
            Log.d("e", e.toString())
            _modifySuccess.postValue(false)
        }
    }

    private fun convertInfoListToJson(info: Info): String {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val adapter = moshi.adapter(Info::class.java)
        return adapter.toJson(info)
    }

    private fun createRequestBodyFromInfoList(info: Info): RequestBody {
        val json = convertInfoListToJson(info)
        return json.toRequestBody("application/json".toMediaType())
    }

}