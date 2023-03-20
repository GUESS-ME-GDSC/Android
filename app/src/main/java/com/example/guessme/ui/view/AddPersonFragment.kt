package com.example.guessme.ui.view

import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.example.guessme.R
import com.example.guessme.common.base.BaseFragment
import com.example.guessme.common.util.Constants.REQUIRED_GALLERY_PERMISSION
import com.example.guessme.common.util.Constants.REQUIRED_RECORD_AUDIO
import com.example.guessme.databinding.FragmentAddPersonBinding
import com.example.guessme.ui.dialog.NoticeDialog
import com.example.guessme.ui.viewmodel.AddPersonViewModel
import java.io.IOException

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
            stopRecording()
        }

        binding.btnAddPersonSpeaker.setOnClickListener {
            startPlaying()
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
            startRecording()
        } else {
            val dialog = NoticeDialog(R.string.dialog_permission)
            dialog.show(requireActivity().supportFragmentManager, "PermissionDialog")
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun startRecording() {
        addPersonViewModel.setFileName("${requireActivity().externalCacheDir!!.absolutePath}/recording.mp3")

        val recorder: MediaRecorder = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(requireContext())
        } else {
            MediaRecorder()
        }

        addPersonViewModel.setRecorder(recorder)

        addPersonViewModel.recorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(addPersonViewModel.fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioSamplingRate(48000)
            setAudioEncodingBitRate(128000)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e("IOException", "prepare() failed")
            }

            addPersonViewModel.setRecordStatus(true)
            start()
        }

    }

    private fun stopRecording() {
        addPersonViewModel.recorder.apply {
            stop()
            reset()
            release()
            addPersonViewModel.setRecordStatus(false)
        }
        addPersonViewModel.setRecorder(null)
    }

    private fun startPlaying() {
        addPersonViewModel.setPlayer(MediaPlayer())

        addPersonViewModel.player.setOnCompletionListener {
            addPersonViewModel.player.apply {
                stop()
                reset()
                release()
            }

            addPersonViewModel.setPlayer(null)
        }

        addPersonViewModel.player.apply {
            try {
                setDataSource(addPersonViewModel.fileName)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e("IOException", "prepare() failed")
                val dialog = NoticeDialog(R.string.dialog_record_error)
                dialog.show(requireActivity().supportFragmentManager, "RecordDialog")
            } catch (e: java.lang.RuntimeException) {
                Log.e("RuntimeException", "runtime exception")
                val dialog = NoticeDialog(R.string.dialog_record_no_file)
                dialog.show(requireActivity().supportFragmentManager, "RecordDialog")
            }
        }
    }

}