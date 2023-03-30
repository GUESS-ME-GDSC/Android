package com.example.guessme.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.guessme.R
import com.example.guessme.common.base.BaseFragment
import com.example.guessme.common.util.GlideApp
import com.example.guessme.databinding.FragmentStartQuizBinding
import com.example.guessme.ui.dialog.NoticeDialog
import com.example.guessme.ui.viewmodel.StartQuizViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StartQuizFragment : BaseFragment<FragmentStartQuizBinding>(R.layout.fragment_start_quiz) {
    private val startQuizFragmentArgs: StartQuizFragmentArgs by navArgs()
    private val startQuizViewModel by activityViewModels<StartQuizViewModel>()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStartQuizBinding {
        return FragmentStartQuizBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserver()
        init()

        CoroutineScope(Dispatchers.IO).launch {
            startQuizViewModel.getPersonQuiz(startQuizFragmentArgs.id)
        }
    }

    private fun setObserver() {
        startQuizViewModel.getPersonQuiz.observe(viewLifecycleOwner) { getPersonQuiz ->
            if (getPersonQuiz) {
                val dialog = NoticeDialog(R.string.dialog_msg_error)
                dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
            }
        }

        startQuizViewModel.quizImage.observe(viewLifecycleOwner) { image ->
            if (image != null) {
                binding.imageQuizProfile.visibility = View.VISIBLE
                binding.viewQuizForProfile.visibility = View.VISIBLE
                GlideApp.with(requireContext()).load(image).into(binding.imageQuizProfile)
            }
        }
    }

    private fun init() {

        binding.btnQuizStart.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_start_quiz_to_quizFragment)
        }

    }



}