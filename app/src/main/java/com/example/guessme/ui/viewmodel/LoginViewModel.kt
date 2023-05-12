package com.example.guessme.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guessme.data.model.User
import com.example.guessme.data.response.BaseNullResponseBody
import com.example.guessme.domain.repository.LocalRepository
import com.example.guessme.domain.repository.LogInRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LogInRepository,
    private val localRepository: LocalRepository
): ViewModel() {
    private val _errorMsg = MutableLiveData<Int>()
    val errorMsg: LiveData<Int> get() = _errorMsg
    private val _errorState = MutableLiveData(false)
    val errorState: LiveData<Boolean> get() = _errorState
    private val _isLogin: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLogin: LiveData<Boolean> get() = _isLogin
    private val _token = MutableLiveData("")
    val token: LiveData<String> = _token

    suspend fun login(user: User) {
        try {
            val response: Response<BaseNullResponseBody> = loginRepository.logIn(user)
            val status = response.body()?.status
            Log.d("status", status.toString())
            val token = response.body()?.data

            if ((status == 201) and response.isSuccessful) {
                //토큰 처리
                Log.d("token", token!!)
                saveToken(token!!)
                _isLogin.postValue(true)
            } else if (status == 400) {
                _errorMsg.postValue(400)
            } else {
                _errorState.postValue(true)
            }

        }catch (e: Exception) {
            Log.d("e", e.toString())
            _errorState.postValue(true)
        }
    }

    private suspend fun saveToken(token: String) {
        localRepository.saveToken(token)
    }

    fun getToken() {
        viewModelScope.launch {
            _token.postValue(localRepository.getToken().first())
        }
    }
}