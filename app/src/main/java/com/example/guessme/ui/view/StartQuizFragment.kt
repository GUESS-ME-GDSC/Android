package com.example.guessme.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.guessme.R
import com.example.guessme.common.base.BaseFragment
import com.example.guessme.databinding.FragmentStartQuizBinding

class StartQuizFragment : BaseFragment<FragmentStartQuizBinding>(R.layout.fragment_start_quiz) {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStartQuizBinding {
        return FragmentStartQuizBinding.inflate(inflater, container, false)
    }



}