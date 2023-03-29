package com.example.guessme.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.guessme.R
import com.example.guessme.common.base.BaseDialog
import com.example.guessme.data.model.Info
import com.example.guessme.databinding.DialogAddInfoBinding
import com.example.guessme.ui.viewmodel.PersonDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.wait


class AddInfoDialog(private val viewModel: PersonDetailViewModel): BaseDialog<DialogAddInfoBinding>(R.layout.dialog_add_info) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.btnAddInfoCancel.setOnClickListener {
            dismiss()
        }

        binding.btnAddInfoSave.setOnClickListener {
            val key = binding.editAddInfoKey.text.toString()
            val value = binding.editAddInfoValue.text.toString()

            if ((key.trim() != "") and (value.trim() != "")) {
                //owner 추후 수정해야 함.
                val info = Info(null, key, value)
                CoroutineScope(Dispatchers.IO).launch {
                    addInfo(info, 2)
                }
            }

        }

        return binding.root
    }

    private suspend fun addInfo(info: Info, id: Int) {
        try {
            viewModel.addInfo(info, id)
            dismiss()
        } catch (e: java.lang.Exception) {
            Log.d("e", e.toString())
            viewModel.setAddSuccess(false)
            dismiss()
        }
    }

    override fun getViewBinding() = DialogAddInfoBinding.inflate(layoutInflater)

}