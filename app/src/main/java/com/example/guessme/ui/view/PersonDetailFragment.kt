package com.example.guessme.ui.view

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guessme.R
import com.example.guessme.common.base.BaseFragment
import com.example.guessme.common.base.BasePlayer
import com.example.guessme.common.util.GlideApp
import com.example.guessme.databinding.FragmentPersonDetailBinding
import com.example.guessme.ui.adapter.InfoListAdapter
import com.example.guessme.ui.dialog.AddInfoDialog
import com.example.guessme.ui.dialog.ModifyInfoDialog
import com.example.guessme.ui.dialog.NoticeDialog
import com.example.guessme.ui.viewmodel.PersonDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class PersonDetailFragment : BaseFragment<FragmentPersonDetailBinding>(R.layout.fragment_person_detail) {
    private val personDetailFragmentArgs: PersonDetailFragmentArgs by navArgs()
    private val personDetailViewModel by viewModels<PersonDetailViewModel>()
    private lateinit var infoListAdapter: InfoListAdapter

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPersonDetailBinding {
        return FragmentPersonDetailBinding.inflate(inflater, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setObserver()

        CoroutineScope(Dispatchers.IO).launch {
            getPerson(personDetailFragmentArgs.id)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setObserver() {
        personDetailViewModel.infoList.observe(viewLifecycleOwner) {
            if (it != null) {
                Log.d("list check", it.toString())
                setupRecyclerView()
                infoListAdapter.submitList(it)
            }
        }

        personDetailViewModel.isDelete.observe(viewLifecycleOwner) {
            infoListAdapter.setDelete(it)
            infoListAdapter.notifyDataSetChanged()
        }

        personDetailViewModel.addSuccess.observe(viewLifecycleOwner) {
            if (it == false) {
                val dialog = NoticeDialog(R.string.dialog_msg_error)
                dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    getPerson(personDetailFragmentArgs.id)
                }
            }
        }

        personDetailViewModel.getPersonSuccess.observe(viewLifecycleOwner) {
            if (! it){
                val dialog = NoticeDialog(R.string.dialog_msg_error)
                dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
            }
        }

        personDetailViewModel.person.observe(viewLifecycleOwner) {
            init()
        }

        personDetailViewModel.deleteSuccess.observe(viewLifecycleOwner) { deleteSuccess ->
            if (! deleteSuccess) {
                val dialog = NoticeDialog(R.string.dialog_msg_error)
                dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    getPerson(personDetailFragmentArgs.id)
                }

                val dialog = NoticeDialog(R.string.detail_info_delete_success)
                dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
            }
        }

        personDetailViewModel.modifySuccess.observe(viewLifecycleOwner) { modifySuccess ->
            if (!modifySuccess) {
                val dialog = NoticeDialog(R.string.dialog_modify_msg_error)
                dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    getPerson(personDetailFragmentArgs.id)
                }

                val dialog = NoticeDialog(R.string.detail_info_modify_success)
                dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun getPerson(id: Int) {
        try {
            personDetailViewModel.getPerson(id)
        } catch (e: java.lang.Exception) {
            val dialog = NoticeDialog(R.string.dialog_msg_error)
            dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun init() {
        personDetailViewModel.person.let { person ->
            val dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd")

            binding.txtDetailName.text = person.value!!.name
            binding.txtDetailRelation.text = person.value!!.relation
            binding.txtDetailBirth.text = person.value!!.birth.format(dateFormat)
            binding.txtDetailAddress.text = person.value!!.residence


            binding.btnDetailSpeaker.setOnClickListener {
                personDetailViewModel.setPlayer(BasePlayer(requireActivity().supportFragmentManager))
                personDetailViewModel.startPlaying(person.value!!.voice)
            }


            person.value!!.image?.let {
                GlideApp.with(requireContext()).load(it).into(binding.imageDetailProfile)
            }

            if (person.value!!.favorite) {
                binding.imageDetailFavoriteTrue.setImageResource(R.drawable.ic_favorite_true)
            } else {
                binding.imageDetailFavoriteTrue.setImageResource(R.drawable.ic_favorite_false)
            }
        }

        binding.btnDetailInfoAdd.setOnClickListener {
            val dialog = AddInfoDialog(personDetailViewModel, personDetailFragmentArgs.id)
            dialog.show(requireActivity().supportFragmentManager, "AddInfoDialog")
        }

        binding.fabDetailPersonModify.setOnClickListener {
            val person = personDetailViewModel.person.value
            val action = PersonDetailFragmentDirections.actionFragmentPersonDetailToModifyPersonFragment(person = person!!, id = personDetailFragmentArgs.id)
            findNavController().navigate(action)
        }

        binding.btnDetailQuiz.setOnClickListener {
            val action = PersonDetailFragmentDirections.actionFragmentPersonDetailToStartQuizFragment(personDetailFragmentArgs.id)
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerView() {
        infoListAdapter = InfoListAdapter()

        infoListAdapter.setOnItemClickListener { info, pos ->
            val dialog = ModifyInfoDialog(pos, personDetailViewModel, info, personDetailFragmentArgs.id)
            dialog.show(requireActivity().supportFragmentManager, "ModifyInfoDialog")
        }

        binding.recyclerDetailInfo.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = infoListAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar!!.show()
    }
}