package com.example.guessme.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.guessme.R
import com.example.guessme.common.base.BaseFragment
import com.example.guessme.common.util.Constants.REQUIRED_GALLERY_PERMISSION
import com.example.guessme.databinding.FragmentAddPersonBinding
import com.example.guessme.ui.dialog.PermissionDialog
import com.example.guessme.ui.viewmodel.AddPersonViewModel

class AddPersonFragment : BaseFragment<FragmentAddPersonBinding>(R.layout.fragment_add_person) {
    private val addPersonViewModel by viewModels<AddPersonViewModel>()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddPersonBinding {
        return FragmentAddPersonBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewAddPersonForImage.setOnClickListener {
            requestGalleryLauncher.launch(REQUIRED_GALLERY_PERMISSION)
        }
    }

    private val requestGalleryLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            getGalleryLauncher.launch("image/*")
        } else {
            val dialog = PermissionDialog()
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
}