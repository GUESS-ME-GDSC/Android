package com.example.guessme.ui.viewmodel

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guessme.common.base.BasePlayer
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
    private val _getPersonQuiz = MutableLiveData<Int>()
    val getPersonQuiz: LiveData<Int> = _getPersonQuiz
    private var answer: IntArray? = null
    private var _cur = MutableLiveData<Int>()
    val cur: LiveData<Int> get() = _cur
    private val _quizImage = MutableLiveData<String?>()
    val quizImage: LiveData<String?> = _quizImage
    private val _quizVoice = MutableLiveData<String?>()
    val quizVoice: LiveData<String?> = _quizVoice
    private var _player: BasePlayer? = null
    val player get() = _player
    private val _personId = MutableLiveData<Int>()
    val personId: LiveData<Int> = _personId
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> = _score
    private var getQuizId: Int? = null

    fun getQuizId(): Int {
        return getQuizId!!
    }

    fun increaseCur() {
        _cur.postValue(_cur.value!!+1)
    }

    fun decreaseCur() {
        _cur.postValue(cur.value!!-1)
    }

    private fun setAnswerList(size: Int) {
        answer = IntArray(size)
    }

    fun setCorrect() {
        answer!![cur.value!!] = 1
    }

    fun setWrong() {
        answer!![cur.value!!] = 0
    }

    fun getScore(): Int {
        return (answer!!.sum() / quizList.value!!.size) * 100
    }

    fun isQuizLast(): Boolean {
        return cur.value == quizList.value!!.size-1
    }

    fun isQuizStart(): Boolean {
        return cur.value == 0
    }

    fun setPlayer(player: BasePlayer) {
        _player = player
        player.setPlayer(MediaPlayer())
    }

    fun startPlaying(fileName: String) {
        player!!.startPlaying(fileName)
    }

    fun getCurQuiz(): Quiz {
        return quizList.value?.get(cur.value!!)!!
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
                getQuizId = id
                _quizImage.postValue(personQuiz.image)
                _quizVoice.postValue(personQuiz.voice)
                _quizList.postValue(personQuiz.quizList)
                _personId.postValue(personQuiz.personId)
                _score.postValue(personQuiz.score)
                _cur.postValue(0)
                setAnswerList(personQuiz.quizList.size)

            } else if (status == 500) {
                _getPersonQuiz.postValue(500)
            } else {
                _getPersonQuiz.postValue(0)
            }
        } catch (e: java.lang.Exception) {
            Log.d("e", e.toString())
            _getPersonQuiz.postValue(0)
        }
    }

}