package com.example.guessme.ui.view

import android.media.MediaPlayer
import android.net.Uri
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
import com.example.guessme.common.util.GlideApp
import com.example.guessme.databinding.FragmentPersonDetailBinding
import com.example.guessme.ui.adapter.InfoListAdapter
import com.example.guessme.ui.dialog.AddInfoDialog
import com.example.guessme.ui.dialog.NoticeDialog
import com.example.guessme.ui.viewmodel.PersonDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class PersonDetailFragment : BaseFragment<FragmentPersonDetailBinding>(R.layout.fragment_person_detail) {
    private val personDetialFragmentargs: PersonDetailFragmentArgs by navArgs()
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
            getPerson(personDetialFragmentargs.id)
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
            if ((it != null) and (it!!.isNotEmpty())) {
                binding.btnDetailInfoDelete.visibility = View.VISIBLE
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

            person.value!!.voice?.let {
                binding.btnDetailSpeaker.setOnClickListener {
                    val audioUri = Uri.parse(person.value!!.voice)
                    val fileName = File(audioUri.path!!).path
                    personDetailViewModel.setPlayer(BasePlayer(requireActivity().supportFragmentManager))
                    personDetailViewModel.startPlaying(fileName)
                }
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
            val dialog = AddInfoDialog(personDetailViewModel, personDetialFragmentargs.id)
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
//            val person = personDetailViewModel.person
//            val infoList = personDetailViewModel.infoList

            //person 넘기는 부분에서 오류 발생!
//            val action = PersonDetailFragmentDirections.actionFragmentPersonDetailToModifyPersonFragment(person = person.value!!, infoList = infoList.value)
//            findNavController().navigate(action)
        }

        binding.btnDetailQuiz.setOnClickListener {
            val action = PersonDetailFragmentDirections.actionFragmentPersonDetailToStartQuizFragment(personDetialFragmentargs.id)
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerView() {
        infoListAdapter = InfoListAdapter()
        binding.recyclerDetailInfo.apply {
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