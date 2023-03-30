package com.example.guessme.ui.view

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.guessme.R
import com.example.guessme.common.base.BaseFragment
import com.example.guessme.databinding.FragmentScoringBinding
import com.example.guessme.ui.dialog.NoticeDialog
import com.example.guessme.ui.viewmodel.ScoringViewModel
import com.example.guessme.ui.viewmodel.StartQuizViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScoringFragment : BaseFragment<FragmentScoringBinding>(R.layout.fragment_scoring) {
    private val startQuizViewModel: StartQuizViewModel by activityViewModels()
    private val scoringFragmentArgs: ScoringFragmentArgs by navArgs()
    private val scoringViewModel: ScoringViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentScoringBinding {
        return FragmentScoringBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserver()
        init()

        CoroutineScope(Dispatchers.IO).launch {
            scoringViewModel.quizScoring(scoringFragmentArgs.imageUri, scoringFragmentArgs.quiz.answer)
        }
    }

    private fun setObserver() {
        scoringViewModel.result.observe(viewLifecycleOwner) { result ->
            if (result) {
                startQuizViewModel.setCorrect()
                binding.txtQuizResult.setText(R.string.quiz_result_correct)
            } else {
                startQuizViewModel.setWrong()
                binding.txtQuizResult.setText(R.string.quiz_result_wrong)
            }
        }

        scoringViewModel.quizScoring.observe(viewLifecycleOwner) { quizScoring ->
            if (! quizScoring) {
                val dialog = NoticeDialog(R.string.dialog_msg_error)
                dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
            }
        }
    }

    private fun init() {
        binding.txtQuizStepNum.text = (startQuizViewModel.cur.value!!+1).toString()

        val uri = Uri.parse(scoringFragmentArgs.imageUri)
        binding.imageQuizUserAnswer.setImageURI(uri)

        val quiz = startQuizViewModel.getCurQuiz()
        Log.d("quiz", quiz.toString())
        binding.txtQuizAnswerKey.text = quiz.question
        binding.txtQuizAnswerValue.text = quiz.answer

        if (startQuizViewModel.isQuizLast()) {
            binding.btnQuizAnswerNext.visibility = View.GONE
        }

        binding.btnQuizAnswerNext.setOnClickListener {
            startQuizViewModel.increaseCur()
            findNavController().navigate(R.id.action_fragment_scoring_to_fragment_quiz)
        }

        binding.btnQuizStop.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_scoring_to_scoreFragment)
        }
    }
}