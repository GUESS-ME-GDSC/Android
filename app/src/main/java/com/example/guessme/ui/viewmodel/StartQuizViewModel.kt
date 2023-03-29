package com.example.guessme.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guessme.data.response.Quiz
import com.example.guessme.data.response.QuizResponseBody
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
    private val _quizList = MutableLiveData<List<Quiz>>()
    private val quizList: LiveData<List<Quiz>> = _quizList
    private val _getPersonQuiz = MutableLiveData<Boolean>()
    val getPersonQuiz: LiveData<Boolean> = _getPersonQuiz
    private var _correctCount: Int = 0
    private val correctCount: Int = _correctCount
    private var _cur: Int = 0
    val cur: Int = _cur
    private val _quizImage = MutableLiveData<String?>()
    val quizImage: LiveData<String?> = _quizImage
    private val _quizVoice = MutableLiveData<String?>()
    val quizVoice: LiveData<String?> = _quizVoice

    fun increaseCur() {
        _cur += 1
    }

    fun increaseCount() {
        _correctCount += 1
    }

    fun getScore(): Int {
        return (correctCount / quizList.value!!.size) * 100
    }

    suspend fun getPersonQuiz(id: Int) {
        try {
            val token = withContext(Dispatchers.IO) {
                localRepository.getToken().first()
            }
            val response: Response<QuizResponseBody> = startQuizRepository.getPersonQuiz("Bearer $token", id)
            val status = response.body()?.status

            if ((status == 200) and response.isSuccessful) {
                val personQuiz = response.body()!!.data

                _quizImage.postValue(personQuiz.image)
                _quizVoice.postValue(personQuiz.voice)
                _quizList.postValue(personQuiz.quizList)

            } else {
                _getPersonQuiz.postValue(false)
            }
        } catch (e: java.lang.Exception) {
            Log.d("e", e.toString())
            _getPersonQuiz.postValue(false)
        }
    }

}