package com.example.guessme.ui.view

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.guessme.R
import com.example.guessme.common.base.BaseFragment
import com.example.guessme.common.base.BasePlayer
import com.example.guessme.common.util.Constants.REQUIRED_CAMERA_PERMISSION
import com.example.guessme.common.util.GlideApp
import com.example.guessme.data.response.Quiz
import com.example.guessme.databinding.FragmentQuizBinding
import com.example.guessme.ui.dialog.NoticeDialog
import com.example.guessme.ui.viewmodel.StartQuizViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class QuizFragment : BaseFragment<FragmentQuizBinding>(R.layout.fragment_quiz) {
    private val startQuizViewModel: StartQuizViewModel by activityViewModels()
    private var photoUri: Uri? = null
    private var quiz: Quiz? = null
    private var step: Int? = null

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentQuizBinding {
        return FragmentQuizBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setObserver()
    }

    private fun setObserver() {
        startQuizViewModel.cur.observe(viewLifecycleOwner) {
            init()
        }
    }

    private fun init() {
        val image = startQuizViewModel.quizImage
        val voice = startQuizViewModel.quizVoice

        step = startQuizViewModel.cur.value
        binding.txtQuizStepNum.text = (step!!+1).toString()

        quiz = startQuizViewModel.getCurQuiz()
        image.value?.let {
            GlideApp.with(requireActivity()).load(it).into(binding.imageQuizStepProfile)
        }


        if (voice.value != null) {
            binding.btnQuizStepVoice.setOnClickListener {
                if (startQuizViewModel.player == null) {
                    startQuizViewModel.setPlayer(BasePlayer(requireActivity().supportFragmentManager))
                }
                startQuizViewModel.startPlaying(voice.value!!)
            }

        } else {
            binding.btnQuizStepVoice.visibility = View.GONE
        }

        binding.txtQuizStepKey.text = quiz!!.question

        binding.btnQuizStepAnswer.setOnClickListener {
            requestCameraLauncher.launch(REQUIRED_CAMERA_PERMISSION)
        }

        if (startQuizViewModel.isQuizLast()) {
            binding.btnQuizStepNext.setOnClickListener {
                val dialog = NoticeDialog(R.string.dialog_quiz_last)
                dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
            }
        } else {
            binding.btnQuizStepNext.setOnClickListener {
                startQuizViewModel.increaseCur()
            }
        }

        if (startQuizViewModel.isQuizStart()) {
            binding.btnQuizStepBefore.setOnClickListener {
                val dialog = NoticeDialog(R.string.dialog_quiz_start)
                dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
            }
        } else {
            binding.btnQuizStepBefore.setOnClickListener {
                startQuizViewModel.decreaseCur()
            }
        }

    }

    private val requestCameraLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()){ isGranted ->
        if (isGranted) {
            val photoFile = File.createTempFile("IMG_", ".jpg",
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES))
            photoUri = FileProvider.getUriForFile(requireContext(),"${requireActivity().packageName}", photoFile)
            imageCaptureLauncher.launch(photoUri)
        }else {
            val dialog = NoticeDialog(R.string.dialog_permission)
            dialog.show( requireActivity().supportFragmentManager,"NoticeDialog")
        }
    }

    private val imageCaptureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()) { isGranted ->
        if(isGranted) {
            val uri = getRealPath(photoUri!!)
            val action = QuizFragmentDirections.actionFragmentQuizToScoringFragment(imageUri = uri!!, quiz = quiz!!)
            findNavController().navigate(action)
        }
    }

    private fun getRealPath(uri: Uri): String?{
        val contentResolver = requireContext().contentResolver
        val filePath = requireContext().applicationInfo.dataDir + File.separator + System.currentTimeMillis() + ".jpg"
        val file = File(filePath)

        try{
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val outputStream = FileOutputStream(file)
            val buf = ByteArray(1024)
            var len: Int
            while(inputStream.read(buf).also { len = it } > 0)
                outputStream.write(buf, 0, len)
            outputStream.close()
            inputStream.close()
        }catch(ignore: IOException){
            return null
        }
        return file.absolutePath
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

}