package com.example.guessme.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guessme.common.util.ScoringState
import com.example.guessme.data.response.ScoringResponseBody
import com.example.guessme.domain.repository.LocalRepository
import com.example.guessme.domain.repository.ScoringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ScoringViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val scoringRepository: ScoringRepository
): ViewModel() {
    private val _result = MutableLiveData<Boolean>()
    val result: LiveData<Boolean> = _result
    private val _quizScoring = MutableLiveData(ScoringState.LOADING)
    val quizScoring: LiveData<ScoringState> = _quizScoring

    suspend fun quizScoring(image: String, infoValue: String, infoKey: String, personId: Int) {
        try {
            val token = withContext(Dispatchers.IO) {
                localRepository.getToken().first()
            }
            Log.d("quizScoring", "$infoValue $infoKey $personId")
           val response = viewModelScope.async {
               scoringRepository.quizScoring("Bearer $token", image, infoValue, infoKey, personId)
           }.await()

            val status = response.body()?.status
            Log.e("scoring result", status.toString())
            Log.e("scoring result", response.body().toString())

            if((status == 200) and response.isSuccessful) {
                _result.postValue( response.body()!!.data!!)
                _quizScoring.postValue(ScoringState.SUCCESS)
            } else if ((status == 400)) {
                _quizScoring.postValue(ScoringState.NO_TEXT)
            } else {
                _quizScoring.postValue(ScoringState.FAIL)
            }
        } catch (e: java.lang.Exception) {
            Log.d("quizScoring", e.toString())
            _quizScoring.postValue(ScoringState.FAIL)
        }
    }

}