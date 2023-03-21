package com.example.guessme.ui.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guessme.R
import com.example.guessme.common.base.BaseFragment
import com.example.guessme.common.base.BasePlayer
import com.example.guessme.data.model.Info
import com.example.guessme.databinding.FragmentPersonDetailBinding
import com.example.guessme.ui.adapter.InfoListAdapter
import com.example.guessme.ui.dialog.AddInfoDialog
import com.example.guessme.ui.viewmodel.PersonDetailViewModel
import java.io.File
import java.time.format.DateTimeFormatter

class PersonDetailFragment : BaseFragment<FragmentPersonDetailBinding>(R.layout.fragment_person_detail) {
    private val args: PersonDetailFragmentArgs by navArgs()
    private val personDetailViewModel by viewModels<PersonDetailViewModel>()
    private lateinit var infoListAdapter: InfoListAdapter

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPersonDetailBinding {
        return FragmentPersonDetailBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        personDetailViewModel.setPerson(args.person)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        infoListAdapter.submitList(getTempInfoData())

        personDetailViewModel.person.let { person ->
            val dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd")
            val fileName = if (person.voice == null) {
                null
            } else {
                File(person.voice.path!!).path
            }

            binding.txtDetailName.text = person.name
            binding.txtDetailRelation.text = person.relation
            binding.txtDetailBirth.text = person.birth.format(dateFormat)
            binding.txtDetailAddress.text = person.residence

            binding.btnDetailSpeaker.setOnClickListener {
                personDetailViewModel.setPlayer(BasePlayer(requireActivity().supportFragmentManager))
                personDetailViewModel.startPlaying(fileName)
            }

            if (person.image != null) {
                binding.imageDetailProfile.setImageURI(person.image)
            }

            if (person.favorite) {
                binding.imageDetailFavoriteTrue.visibility = View.VISIBLE
            }

        }

        binding.btnDetailInfoAdd.setOnClickListener {
            val dialog = AddInfoDialog()
            dialog.show(requireActivity().supportFragmentManager, "AddInfoDialog")
        }
    }

    private fun setupRecyclerView() {
        infoListAdapter = InfoListAdapter()
        binding.recyclerDetailInfo.apply {
            setHasFixedSize(true)
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

    private fun getTempInfoData(): ArrayList<Info> {
        val data = ArrayList<Info>()

        data.add(Info(null, 1, "취미", "운동"))
        data.add(Info(null, 1, "나이", "24"))

        return data
    }

}