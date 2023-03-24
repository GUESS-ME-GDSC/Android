package com.example.guessme.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.guessme.R
import com.example.guessme.common.base.BaseFragment
import com.example.guessme.databinding.FragmentStartQuizBinding
import com.example.guessme.ui.viewmodel.StartQuizViewModel

class StartQuizFragment : BaseFragment<FragmentStartQuizBinding>(R.layout.fragment_start_quiz) {
    private val startQuizFragmentArgs: StartQuizFragmentArgs by navArgs()
    private val startQuizViewModel by viewModels<StartQuizViewModel>()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStartQuizBinding {
        return FragmentStartQuizBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startQuizViewModel.setPerson(startQuizFragmentArgs.person)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val person = startQuizViewModel.person

        person.image?.let {
            binding.imageQuizProfile.setImageURI(it)
        }

        binding.btnQuizStart.setOnClickListener {
            //info 넘겨줌
            //퀴즈 화면으로 이동 필요
        }
    }



}