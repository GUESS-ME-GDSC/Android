package com.example.guessme.ui.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guessme.R
import com.example.guessme.common.base.BaseFragment
import com.example.guessme.common.base.BasePlayer
import com.example.guessme.common.base.BaseRecorder
import com.example.guessme.common.util.Constants
import com.example.guessme.common.util.GlideApp
import com.example.guessme.databinding.FragmentAddModifyPersonBinding
import com.example.guessme.ui.adapter.InfoModifyAdapter
import com.example.guessme.ui.dialog.NoticeDialog
import com.example.guessme.ui.viewmodel.ModifyPersonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ModifyPersonFragment: BaseFragment<FragmentAddModifyPersonBinding>(R.layout.fragment_add_modify_person) {
    private val modifyPersonViewModel by viewModels<ModifyPersonViewModel>()
    private val modifyPersonFragmentArgs: ModifyPersonFragmentArgs by navArgs()
    private lateinit var infoModifyAdapter: InfoModifyAdapter

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddModifyPersonBinding {
        return FragmentAddModifyPersonBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        modifyPersonViewModel.setPerson(modifyPersonFragmentArgs.person)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setObserver()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setObserver() {
        modifyPersonViewModel.person.observe(viewLifecycleOwner) {
            init()
        }

        modifyPersonViewModel.infoList.observe(viewLifecycleOwner) { infoList ->
            infoModifyAdapter.submitList(infoList)
        }

        modifyPersonViewModel.favorite.observe(viewLifecycleOwner) { favorite ->
            if (favorite) {
                binding.imageModifyFavoriteTrue.setImageResource(R.drawable.ic_favorite_true)
            } else {
                binding.imageModifyFavoriteTrue.setImageResource(R.drawable.ic_favorite_false)
            }
        }

        modifyPersonViewModel.deleteSuccess.observe(viewLifecycleOwner) { deleteSuccess ->
            if (deleteSuccess) {
                val dialog = NoticeDialog(R.string.detail_info_delete_success)
                dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")

                findNavController().navigate(R.id.action_fragment_modify_person_to_fragment_people_list)

            } else {
                val dialog = NoticeDialog(R.string.dialog_msg_error)
                dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun init() {
        val person = modifyPersonViewModel.person.value!!
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val localDate = LocalDate.parse(person.birth, format)

        binding.btnAddPersonDelete.visibility = View.VISIBLE
        binding.recyclerModifyInfo.visibility = View.VISIBLE

        person.image?.let {
            GlideApp.with(requireActivity()).load(it).into(binding.imageAddPersonProfile)
        }

        person.voice?.let {
            modifyPersonViewModel.setFileName(it)
        }

        binding.imageModifyFavoriteTrue.visibility = View.VISIBLE
        if (person.favorite) {
            binding.imageModifyFavoriteTrue.setImageResource(R.drawable.ic_favorite_true)
        } else {
            binding.imageModifyFavoriteTrue.setImageResource(R.drawable.ic_favorite_false)
        }

        binding.editAddPersonName.setText(person.name)
        binding.editAddPersonRelation.setText(person.relation)
        binding.editAddPersonBirth.updateDate(localDate.year, localDate.monthValue-1, localDate.dayOfMonth)
        binding.editAddPersonAddress.setText(person.residence)

        binding.viewAddPersonForImage.setOnClickListener {
            requestGalleryLauncher.launch(Constants.REQUIRED_GALLERY_PERMISSION)
        }

        binding.btnAddPersonRecordStart.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requestRecordLauncher.launch(Constants.REQUIRED_RECORD_AUDIO)
            }
        }

        binding.btnAddPersonRecordStop.setOnClickListener {
            modifyPersonViewModel.stopRecording()
        }


        binding.btnAddPersonSpeaker.setOnClickListener {
            modifyPersonViewModel.setPlayer(BasePlayer(requireActivity().supportFragmentManager))
            modifyPersonViewModel.startPlaying(modifyPersonViewModel.fileName)
        }

        modifyPersonViewModel.recordStatus.observe(viewLifecycleOwner) { status ->
            if (status) {
                binding.btnAddPersonRecordStart.visibility = View.GONE
                binding.btnAddPersonRecordStop.visibility = View.VISIBLE
            } else {
                binding.btnAddPersonRecordStart.visibility = View.VISIBLE
                binding.btnAddPersonRecordStop.visibility = View.GONE
            }
        }

        binding.imageModifyFavoriteTrue.setOnClickListener {
            modifyPersonViewModel.setFavorite(!modifyPersonViewModel.favorite.value!!)
        }

        binding.btnAddPersonDelete.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                deletePerson()
            }
        }
    }

    private suspend fun deletePerson() {
        try {
            modifyPersonViewModel.deletePerson(modifyPersonFragmentArgs.id)
        } catch (e: java.lang.Exception) {
            val dialog = NoticeDialog(R.string.dialog_msg_error)
            dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
        }
    }

    private fun setupRecyclerView() {
        infoModifyAdapter = InfoModifyAdapter()
        binding.recyclerModifyInfo.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = infoModifyAdapter
        }
    }

    private val requestGalleryLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            getGalleryLauncher.launch("image/*")
        } else {
            val dialog = NoticeDialog(R.string.dialog_permission)
            dialog.show(requireActivity().supportFragmentManager, "PermissionDialog")
        }
    }

    private val getGalleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            modifyPersonViewModel.setImage(uri)
            binding.imageAddPersonProfile.setImageURI(uri)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private val requestRecordLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            modifyPersonViewModel.setFileName("${requireActivity().externalCacheDir!!.absolutePath}/recording.mp3")
            modifyPersonViewModel.setRecorder(BaseRecorder(), requireContext())
            modifyPersonViewModel.startRecording(modifyPersonViewModel.fileName!!)
        } else {
            val dialog = NoticeDialog(R.string.dialog_permission)
            dialog.show(requireActivity().supportFragmentManager, "PermissionDialog")
        }
    }
}