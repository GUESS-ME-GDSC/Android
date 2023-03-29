package com.example.guessme.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guessme.data.response.PeopleListResponseBody
import com.example.guessme.data.response.PersonPreview
import com.example.guessme.domain.repository.LocalRepository
import com.example.guessme.domain.repository.PeopleListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PeopleListViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val peopleListRepository: PeopleListRepository
): ViewModel() {
    private val _favoritePeopleList = MutableLiveData<List<PersonPreview>>()
    val favoritePeopleList: LiveData<List<PersonPreview>> = _favoritePeopleList
    private val _notFavoritePeopleList = MutableLiveData<List<PersonPreview>>()
    val notFavoritePeopleList: LiveData<List<PersonPreview>> = _notFavoritePeopleList
    private val _getPeopleList = MutableLiveData<Boolean>()
    val getPeopleList: LiveData<Boolean> = _getPeopleList

    suspend fun getPeopleList(favorite: Boolean) {
        try {
            val token = withContext(Dispatchers.IO) {
                localRepository.getToken().first()
            }
            val response: Response<PeopleListResponseBody> = peopleListRepository.getPeopleList("Bearer $token", favorite)
            val status = response.body()?.status

            Log.d("status", status.toString())

            if((status == 200) and response.isSuccessful) {
                if (favorite) {
                    _favoritePeopleList.postValue(response.body()?.data)
                } else {
                    _notFavoritePeopleList.postValue(response.body()?.data)
                }
            } else {
                _getPeopleList.postValue(false)
            }
        } catch (e: java.lang.Exception) {
            Log.d("e", e.toString())
            _getPeopleList.postValue(false)
        }
    }
}