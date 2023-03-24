package com.example.guessme.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guessme.R
import com.example.guessme.data.model.User
import com.example.guessme.data.repository.SignUpRepositoryImpl
import com.example.guessme.data.response.LoginResponseBody
import retrofit2.Response
import java.lang.Exception

class SignUpViewModel: ViewModel(){
    private val repository: SignUpRepositoryImpl = SignUpRepositoryImpl()
    private val _isSignUp: MutableLiveData<Boolean> = MutableLiveData(false)
    val isSignUp: LiveData<Boolean> = _isSignUp
    private val _errorState: MutableLiveData<Int> = MutableLiveData<Int>()
    val errorState: LiveData<Int> = _errorState

    suspend fun signUp(user: User) {
        try {
            val response: Response<LoginResponseBody> = repository.signUp(user)
            val status = response.body()?.status

            if (response.isSuccessful and (status == 200)) {
                _isSignUp.postValue(true)
            } else if (status == 500) {
                _errorState.postValue(R.string.dialog_existed_user)
            } else {
                _errorState.postValue(R.string.dialog_msg_error)
            }

        } catch (e: Exception) {
            _errorState.postValue(R.string.dialog_msg_error)
        }
    }
}