package com.example.guessme.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.guessme.data.model.Person

class PersonDetailViewModel: ViewModel() {
    private var _person: Person? = null
    val person get() = _person!!

    fun setPerson(data: Person?) {
        _person = data
    }
}