package com.example.guessme.ui.view

import android.net.Uri
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StartQuizFragment : BaseFragment<FragmentStartQuizBinding>(R.layout.fragment_start_quiz) {
    private val startQuizFragmentArgs: StartQuizFragmentArgs by navArgs()
    private val startQuizViewModel by viewModels<StartQuizViewModel>()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStartQuizBinding {
        return FragmentStartQuizBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserver()

        CoroutineScope(Dispatchers.IO).launch {
            startQuizViewModel.getPersonQuiz(startQuizFragmentArgs.id)
        }
    }

    private fun setObserver() {
        startQuizViewModel.personImage.observe(viewLifecycleOwner) {
            val uri = Uri.parse(it)
            binding.imageQuizProfile.setImageURI(uri)
        }
    }

    private fun init() {

        binding.btnQuizStart.setOnClickListener {
            //info 넘겨줌
            //퀴즈 화면으로 이동 필요
        }

    }



}