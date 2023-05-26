package com.example.guessme.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guessme.data.response.ScoringResponseBody
import com.example.guessme.domain.repository.LocalRepository
import com.example.guessme.domain.repository.ScoringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ScoringViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val scoringRepository: ScoringRepository
): ViewModel() {
    private val _result = MutableLiveData<Boolean>()
    val result: LiveData<Boolean> = _result
    private val _quizScoring = MutableLiveData<Boolean>()
    val quizScoring: LiveData<Boolean> = _quizScoring

    suspend fun quizScoring(image: String, infoValue: String, infoKey: String, personId: Int) {
        try {
            val token = withContext(Dispatchers.IO) {
                localRepository.getToken().first()
            }

            val response: Response<ScoringResponseBody> = scoringRepository.quizScoring("Bearer $token", image, infoValue, infoKey, personId)
            val status = response.body()?.status
            Log.e("scoring result", status.toString())

            if((status == 200) and response.isSuccessful) {
                _result.postValue( response.body()!!.data!!)
            } else {
                _quizScoring.postValue(false)
            }
        } catch (e: java.lang.Exception) {
            Log.d("e", e.toString())
            _quizScoring.postValue(false)
        }
    }

}