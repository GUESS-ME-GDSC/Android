package com.example.guessme.ui.view

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guessme.R
import com.example.guessme.common.base.BaseFragment
import com.example.guessme.common.base.BasePlayer
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
        personDetailViewModel.getInfoList()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        init()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun init() {
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
                binding.imageDetailFavoriteTrue.setImageResource(R.drawable.ic_favorite_true)
            } else {
                binding.imageDetailFavoriteTrue.setImageResource(R.drawable.ic_favorite_false)
            }

        }

        binding.btnDetailInfoAdd.setOnClickListener {
            val dialog = AddInfoDialog()
            dialog.show(requireActivity().supportFragmentManager, "AddInfoDialog")
        }

        binding.btnDetailInfoDelete.setOnClickListener {
            binding.btnDetailInfoAdd.visibility = View.GONE
            binding.btnDetailInfoDelete.visibility = View.GONE
            binding.btnDetailDeleteComplete.visibility = View.VISIBLE
            personDetailViewModel.setDelete(true)
        }

        binding.btnDetailDeleteComplete.setOnClickListener {
            personDetailViewModel.setDelete(false)
            //delete 값 서버에 전달 처리
            Log.d("list", infoListAdapter.deleteSet.toString())
            binding.btnDetailDeleteComplete.visibility = View.GONE
            binding.btnDetailInfoAdd.visibility = View.VISIBLE
            binding.btnDetailInfoDelete.visibility = View.VISIBLE
            infoListAdapter.setDelete(false)
        }

        binding.fabDetailPersonModify.setOnClickListener {
            val person = personDetailViewModel.person
            val infoList = personDetailViewModel.infoList

            val action = PersonDetailFragmentDirections.actionFragmentPersonDetailToModifyPersonFragment(person = person, infoList = infoList.value)
            findNavController().navigate(action)
        }

        personDetailViewModel.infoList.observe(viewLifecycleOwner) {
            infoListAdapter.submitList(it.data)
            if (it.data.size > 0) {
                binding.btnDetailInfoDelete.visibility = View.VISIBLE
            }
        }

        personDetailViewModel.isDelete.observe(viewLifecycleOwner) {
            infoListAdapter.setDelete(it)
            infoListAdapter.notifyDataSetChanged()
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

}