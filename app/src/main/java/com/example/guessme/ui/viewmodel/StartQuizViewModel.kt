package com.example.guessme.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guessme.data.response.PersonResponse
import com.example.guessme.domain.repository.LocalRepository
import com.example.guessme.domain.repository.StartQuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class StartQuizViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val startQuizRepository: StartQuizRepository
): ViewModel() {
    private val _getPersonQuiz = MutableLiveData<Boolean>()
    val getPersonQuiz: LiveData<Boolean> = _getPersonQuiz
    private val _personImage = MutableLiveData<String>()
    val personImage: LiveData<String> = _personImage


    suspend fun getPersonQuiz(id: Int) {
        try {
            val token = withContext(Dispatchers.IO) {
                localRepository.getToken().first()
            }
            val response: Response<PersonResponse> = startQuizRepository.getPersonQuiz("Bearer $token", id)
            val status = response.body()?.status
            Log.d("data", response.body()!!.data.toString())

            if ((status == 200) and response.isSuccessful) {
                val person = response.body()!!.data

                person.image?.let { uri ->
                    _personImage.postValue(uri)
                }
            } else {
                _getPersonQuiz.postValue(false)
            }
        } catch (e: java.lang.Exception) {
            Log.d("e", e.toString())
            _getPersonQuiz.postValue(false)
        }
    }

}