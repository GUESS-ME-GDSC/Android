package com.example.guessme.ui.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.example.guessme.R
import com.example.guessme.common.base.BaseFragment
import com.example.guessme.common.base.BasePlayer
import com.example.guessme.common.base.BaseRecorder
import com.example.guessme.common.util.Constants.REQUIRED_GALLERY_PERMISSION
import com.example.guessme.common.util.Constants.REQUIRED_RECORD_AUDIO
import com.example.guessme.databinding.FragmentAddPersonBinding
import com.example.guessme.ui.dialog.NoticeDialog
import com.example.guessme.ui.viewmodel.AddPersonViewModel

class AddPersonFragment : BaseFragment<FragmentAddPersonBinding>(R.layout.fragment_add_person) {
    private val addPersonViewModel by viewModels<AddPersonViewModel>()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddPersonBinding {
        return FragmentAddPersonBinding.inflate(inflater, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewAddPersonForImage.setOnClickListener {
            requestGalleryLauncher.launch(REQUIRED_GALLERY_PERMISSION)
        }

        binding.btnAddPersonRecordStart.setOnClickListener {
            requestRecordLauncher.launch(REQUIRED_RECORD_AUDIO)
        }

        binding.btnAddPersonRecordStop.setOnClickListener {
            addPersonViewModel.stopRecording()
        }

        binding.btnAddPersonSpeaker.setOnClickListener {
            addPersonViewModel.setPlayer(BasePlayer(requireActivity().supportFragmentManager))
            addPersonViewModel.startPlaying(addPersonViewModel.fileName)
        }

        addPersonViewModel.recordStatus.observe(viewLifecycleOwner) { status ->
            if (status) {
                binding.btnAddPersonRecordStart.visibility = View.GONE
                binding.btnAddPersonRecordStop.visibility = View.VISIBLE
            } else {
                binding.btnAddPersonRecordStart.visibility = View.VISIBLE
                binding.btnAddPersonRecordStop.visibility = View.GONE
            }
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
            addPersonViewModel.setImage(uri)
            binding.imageAddPersonProfile.setImageURI(uri)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private val requestRecordLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            addPersonViewModel.setFileName("${requireActivity().externalCacheDir!!.absolutePath}/recording.mp3")
            addPersonViewModel.setRecorder(BaseRecorder(), requireContext())
            addPersonViewModel.startRecording(addPersonViewModel.fileName!!)
        } else {
            val dialog = NoticeDialog(R.string.dialog_permission)
            dialog.show(requireActivity().supportFragmentManager, "PermissionDialog")
        }
    }

}