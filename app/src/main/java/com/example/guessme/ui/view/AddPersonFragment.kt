package com.example.guessme.ui.view

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.guessme.R
import com.example.guessme.common.base.BaseFragment
import com.example.guessme.common.base.BasePlayer
import com.example.guessme.common.base.BaseRecorder
import com.example.guessme.common.util.Constants.REQUIRED_EXTERNAL_STORAGE
import com.example.guessme.common.util.Constants.REQUIRED_GALLERY_PERMISSION
import com.example.guessme.common.util.Constants.REQUIRED_RECORD_AUDIO
import com.example.guessme.data.model.Person
import com.example.guessme.databinding.FragmentAddModifyPersonBinding
import com.example.guessme.ui.dialog.NoticeDialog
import com.example.guessme.ui.viewmodel.AddPersonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

@AndroidEntryPoint
class AddPersonFragment : BaseFragment<FragmentAddModifyPersonBinding>(R.layout.fragment_add_modify_person) {
    private val addPersonViewModel by viewModels<AddPersonViewModel>()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddModifyPersonBinding {
        return FragmentAddModifyPersonBinding.inflate(inflater, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewAddPersonForImage.setOnClickListener{
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                requestGalleryLauncher.launch(REQUIRED_GALLERY_PERMISSION)
            } else {
                requestGalleryLauncher.launch(REQUIRED_EXTERNAL_STORAGE)
            }
        }

        binding.btnAddPersonRecordStart.setOnClickListener{
            requestRecordLauncher.launch(REQUIRED_RECORD_AUDIO)
        }

        binding.btnAddPersonRecordStop.setOnClickListener{
            addPersonViewModel.stopRecording()
        }

        binding.btnAddPersonSave.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch{
                val name = binding.editAddPersonName.text.toString()
                val relation = binding.editAddPersonRelation.text.toString()
                val residence = binding.editAddPersonAddress.text.toString()
                val year = binding.editAddPersonBirth.year
                val month = binding.editAddPersonBirth.month
                val day = binding.editAddPersonBirth.dayOfMonth

                if ((name.trim() != "") and
                    (relation.trim() != "") and
                    (residence.trim() != "")) {

                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val localDate = if (month < 10) {
                        LocalDate.parse("$year-0$month-$day", formatter)
                    } else {
                        LocalDate.parse("$year-$month-$day", formatter)
                    }

                    var voice: Uri? = null
                    addPersonViewModel.fileName?.let{
                        voice = Uri.parse(addPersonViewModel.fileName)
                    }

                    val person = Person(
                        null,
                        false,
                        addPersonViewModel.imageUri,
                        voice,
                        null,
                        name,
                        relation,
                        localDate,
                        residence,
                        null
                    )

                    Log.d("person", person.toString())

                    addPerson(person)
                } else {
                    val dialog = NoticeDialog(R.string.dialog_add_person)
                    dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
                }
            }
        }

        binding.btnAddPersonSpeaker.setOnClickListener{
            addPersonViewModel.setPlayer(BasePlayer(requireActivity().supportFragmentManager))
            addPersonViewModel.startPlaying(addPersonViewModel.fileName)
        }

        addPersonViewModel.recordStatus.observe(viewLifecycleOwner){status->
            if (status) {
                binding.btnAddPersonRecordStart.visibility= View.GONE
                binding.btnAddPersonRecordStop.visibility= View.VISIBLE
            } else {
                binding.btnAddPersonRecordStart.visibility= View.VISIBLE
                binding.btnAddPersonRecordStop.visibility= View.GONE
            }
        }

        addPersonViewModel.addSuccess.observe(viewLifecycleOwner){isSuccess->
            if (isSuccess) {
                findNavController().navigate(R.id.action_fragment_add_person_to_fragment_people_list)
            }
        }

        addPersonViewModel.errorState.observe(viewLifecycleOwner){code->
            when(code) {
                401 -> {
                    val dialog = NoticeDialog(R.string.dialog_token_request)
                    dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
                }
                else -> {
                    val dialog = NoticeDialog(R.string.dialog_msg_error)
                    dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
                }
            }
        }
    }

    private fun getRealPath(uri: Uri): String?{
        val contentResolver = requireContext().contentResolver
        val filePath = requireContext().applicationInfo.dataDir + File.separator+ System.currentTimeMillis() + ".jpg"
        val file = File(filePath)

        try{
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val outputStream = FileOutputStream(file)
            val buf = ByteArray(1024)
            var len: Int
            while(inputStream.read(buf).also{len =it }> 0)
                outputStream.write(buf, 0, len)
            outputStream.close()
            inputStream.close()
        }catch(ignore: IOException){
            return null
        }
        return file.absolutePath
    }

    private suspend fun addPerson(person: Person) {
        try {
            var image: File? = null
            person.image?.let{
                image = File(getRealPath(it)!!)
            }
            addPersonViewModel.addPerson(person, image)
        } catch (e: java.lang.Exception) {
            val dialog = NoticeDialog(R.string.dialog_msg_error)
            dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
        }
    }

    private val requestGalleryLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()){isGranted->
        if (isGranted) {
            getGalleryLauncher.launch("image/*")
        } else {
            val dialog = NoticeDialog(R.string.dialog_permission)
            dialog.show(requireActivity().supportFragmentManager, "PermissionDialog")
        }
    }

    private val getGalleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()){uri->
        uri?.let{
            addPersonViewModel.setImage(uri)
            binding.imageAddPersonProfile.setImageURI(uri)

        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private val requestRecordLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()){isGranted->
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