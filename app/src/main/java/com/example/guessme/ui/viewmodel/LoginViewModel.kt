package com.example.guessme.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guessme.data.model.User
import com.example.guessme.data.response.BaseResponseBody
import com.example.guessme.domain.repository.LocalRepository
import com.example.guessme.domain.repository.LogInRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LogInRepository,
    private val localRepository: LocalRepository
): ViewModel() {
    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> get() = _errorMsg
    private val _errorState = MutableLiveData(false)
    val errorState: LiveData<Boolean> get() = _errorState
    private val _isLogin: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLogin: LiveData<Boolean> get() = _isLogin

    suspend fun login(user: User) {
        try {
            val response: Response<BaseResponseBody> = loginRepository.logIn(user)
            val status = response.body()?.status
            val token = response.body()?.data

            if ((status == 200) and response.isSuccessful) {
                //토큰 처리
                Log.d("token", token!!)
                saveToken(token!!)
                _isLogin.postValue(true)
            } else {
                _errorMsg.postValue(response.body()?.data)
            }

        }catch (e: Exception) {
            _errorState.postValue(true)
        }
    }

    private suspend fun saveToken(token: String) {
        localRepository.saveToken(token)
    }
}