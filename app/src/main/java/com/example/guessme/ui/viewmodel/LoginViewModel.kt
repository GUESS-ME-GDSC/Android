package com.example.guessme.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guessme.R
import com.example.guessme.data.model.User
import com.example.guessme.data.repository.LogInRepositoryImpl
import com.example.guessme.data.response.LoginResponseBody
import retrofit2.Response
import java.lang.Exception

class LoginViewModel: ViewModel() {
    private val repository: LogInRepositoryImpl = LogInRepositoryImpl()
    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> get() = _errorMsg
    private val _errorState = MutableLiveData<Boolean>(false)
    val errorState: LiveData<Boolean> get() = _errorState
    private val _isLogin: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLogin: LiveData<Boolean> get() = _isLogin

    suspend fun login(user: User) {
        try {
            val response: Response<LoginResponseBody> = repository.logIn(user)
            val status = response.body()?.status

            if ((status == 200) and response.isSuccessful) {
                //토큰 처리
                _isLogin.postValue(true)
            } else {
                _errorMsg.postValue(response.body()?.data)
            }

        }catch (e: Exception) {
            _errorState.postValue(true)
        }
    }
}