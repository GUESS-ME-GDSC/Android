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
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.guessme.R
import com.example.guessme.common.base.BaseFragment
import com.example.guessme.common.base.BasePlayer
import com.example.guessme.common.base.BaseRecorder
import com.example.guessme.common.util.Constants
import com.example.guessme.common.util.GlideApp
import com.example.guessme.data.model.Person
import com.example.guessme.databinding.FragmentAddModifyPersonBinding
import com.example.guessme.ui.dialog.NoticeDialog
import com.example.guessme.ui.viewmodel.ModifyPersonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ModifyPersonFragment: BaseFragment<FragmentAddModifyPersonBinding>(R.layout.fragment_add_modify_person) {
    private val modifyPersonViewModel by viewModels<ModifyPersonViewModel>()
    private val modifyPersonFragmentArgs: ModifyPersonFragmentArgs by navArgs()

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
        setObserver()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setObserver() {
        modifyPersonViewModel.person.observe(viewLifecycleOwner) {
            init()
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

        modifyPersonViewModel.modifySuccess.observe(viewLifecycleOwner) { modifySuccess ->
            if (modifySuccess) {
                val dialog = NoticeDialog(R.string.detail_info_modify_success)
                dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")

                val action = ModifyPersonFragmentDirections.actionFragmentModifyPersonToFragmentPersonDetail(modifyPersonFragmentArgs.id)
                findNavController().navigate(action)

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

        person.image?.let {
            GlideApp.with(requireActivity()).load(it).into(binding.imageAddPersonProfile)
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
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                requestGalleryLauncher.launch(Constants.REQUIRED_GALLERY_PERMISSION)
            } else {
                requestGalleryLauncher.launch(Constants.REQUIRED_EXTERNAL_STORAGE)
            }
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

        binding.btnAddPersonSave.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                getPerson()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun getPerson() {
        try {
            val name = binding.editAddPersonName.text.toString()
            val relation = binding.editAddPersonRelation.text.toString()
            val residence = binding.editAddPersonAddress.text.toString()
            val year = binding.editAddPersonBirth.year
            val month = binding.editAddPersonBirth.month
            val day = binding.editAddPersonBirth.dayOfMonth
            val favorite = modifyPersonViewModel.favorite.value

            if ((name.trim() != "") and
                (relation.trim() != "") and
                (residence.trim() != "")) {

                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val localDate = if (month < 10) {
                    if (day < 10) {
                        LocalDate.parse("$year-0${month+1}-0$day", formatter)
                    } else {
                        LocalDate.parse("$year-0${month+1}-$day", formatter)
                    }

                } else {
                    if (day < 10) {
                        LocalDate.parse("$year-${month+1}-0$day", formatter)
                    } else {
                        LocalDate.parse("$year-${month+1}-$day", formatter)
                    }
                }

                var voice: Uri? = null
                modifyPersonViewModel.fileName?.let {
                    voice = Uri.parse(modifyPersonViewModel.fileName)
                }

                val person = Person(
                    null,
                    favorite!!,
                    modifyPersonViewModel.imageUri,
                    voice,
                    null,
                    name,
                    relation,
                    localDate,
                    residence,
                    null
                )
                if (favorite != modifyPersonFragmentArgs.person.favorite){
                    Log.e("favorite", "$favorite, ${modifyPersonFragmentArgs.person.favorite}")
                    modifyPersonFavorite()
                }
                modifyPerson(person)
            }

        } catch (e: java.lang.Exception) {
            Log.e("get modify person error", e.toString())
            val dialog = NoticeDialog(R.string.dialog_msg_error)
            dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
        }
    }

    private suspend fun modifyPerson(person: Person) {
        try {
            var image: File? = null
            person.image?.let {
                image = File(it.toString())
            }

            modifyPersonViewModel.modifyPerson(modifyPersonFragmentArgs.id, person, image)
        } catch (e: java.lang.Exception) {
            Log.e("modify person ", e.toString())
            val dialog = NoticeDialog(R.string.dialog_msg_error)
            dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
        }
    }

    private suspend fun modifyPersonFavorite() {
        try {
            modifyPersonViewModel.modifyPersonFavorite(modifyPersonFragmentArgs.id)
        } catch (e: java.lang.Exception) {
            Log.e("modify person favorite", e.toString())
            val dialog = NoticeDialog(R.string.dialog_msg_error)
            dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
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
            Log.e("modify person getRealPath error", ignore.toString())
            return null
        }
        return file.absolutePath
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
            val realPath = getRealPath(uri)
            modifyPersonViewModel.setImage(realPath!!.toUri())
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