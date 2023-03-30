package com.example.guessme.ui.view

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guessme.R
import com.example.guessme.common.base.BaseFragment
import com.example.guessme.common.base.BasePlayer
import com.example.guessme.common.util.GlideApp
import com.example.guessme.databinding.FragmentScoreBinding
import com.example.guessme.ui.adapter.InfoListAdapter
import com.example.guessme.ui.dialog.NoticeDialog
import com.example.guessme.ui.viewmodel.ScoreViewModel
import com.example.guessme.ui.viewmodel.StartQuizViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ScoreFragment : BaseFragment<FragmentScoreBinding>(R.layout.fragment_score) {
    private val startQuizViewModel: StartQuizViewModel by activityViewModels()
    private val scoreViewModel: ScoreViewModel by viewModels()
    private lateinit var infoListAdapter: InfoListAdapter

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentScoreBinding {
        return FragmentScoreBinding.inflate(inflater, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setObserver()

        CoroutineScope(Dispatchers.IO).launch {
            getPerson(startQuizViewModel.personId.value!!)
        }
    }

    private fun setupRecyclerView() {
        infoListAdapter = InfoListAdapter()
        binding.recyclerResultInfo.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = infoListAdapter
        }
    }

    private suspend fun getPerson(id: Int) {
        try {
            scoreViewModel.getPerson(id)
        } catch (e: java.lang.Exception) {
            val dialog = NoticeDialog(R.string.dialog_msg_error)
            dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setObserver() {
        scoreViewModel.person.observe(viewLifecycleOwner) {
            init()
        }

        scoreViewModel.getPerson.observe(viewLifecycleOwner) { getPerson ->
            if (! getPerson) {
                val dialog = NoticeDialog(R.string.dialog_msg_error)
                dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
            }
        }

        scoreViewModel.infoList.observe(viewLifecycleOwner) { infoList ->
            infoListAdapter.submitList(infoList)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun init() {
        val person = scoreViewModel.person.value!!
        val dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd")

        person.image?.let {
            GlideApp.with(requireActivity()).load(it).into(binding.imageQuizResultProfile)
        }

        binding.txtResultName.text = person.name
        binding.txtResultRelation.text = person.relation
        binding.txtResultBirth.text = person.birth.format(dateFormat)
        binding.txtResultAddress.text = person.residence

        if (person.voice == null) {
            binding.btnQuizStepVoice.visibility = View.GONE
        } else {
            val audioUri = Uri.parse(person.voice)
            val fileName = File(audioUri.path!!).path

            scoreViewModel.setPlayer(BasePlayer(requireActivity().supportFragmentManager))
            binding.btnQuizStepVoice.setOnClickListener {
                scoreViewModel.startPlaying(fileName)
            }
        }

        val beforeScore = startQuizViewModel.score.value!!
        val curScore = startQuizViewModel.getScore()
        binding.txtQuizScore.text = curScore.toString()
        if (beforeScore > curScore) {
            binding.txtQuizScoreDiff.text = (beforeScore - curScore).toString()
            binding.txtQuizDown.visibility = View.VISIBLE
        } else {
            binding.txtQuizScoreDiff.text = (curScore - beforeScore).toString()
            binding.txtQuizUp.visibility = View.VISIBLE
        }

        binding.btnQuizFinish.setOnClickListener {
            val start = startQuizViewModel.getQuizId()



            if (start == 0) {
                findNavController().navigate(R.id.action_fragment_score_to_fragment_home)
            } else {
                val action = ScoreFragmentDirections.actionFragmentScoreToFragmentPersonDetail(startQuizViewModel.personId.value!!)
                findNavController().navigate(action)
            }
        }
    }
}