package com.example.guessme.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.guessme.R
import com.example.guessme.common.base.BaseDialog
import com.example.guessme.data.model.IdList
import com.example.guessme.data.model.Info
import com.example.guessme.databinding.DialogModifyInfoBinding
import com.example.guessme.ui.viewmodel.PersonDetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ModifyInfoDialog(private val viewModel: PersonDetailViewModel, private val info: Info): BaseDialog<DialogModifyInfoBinding>(R.layout.dialog_modify_info) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        init()

        binding.btnModifyInfo.setOnClickListener {

        }

        binding.btnDeleteInfo.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val list = IdList(listOf(info.uuid!!))
                    viewModel.deleteInfo(list)
                    dismiss()
                } catch (e: java.lang.Exception) {
                }
            }
        }

        return binding.root
    }

    private fun init() {
        binding.editAddInfoKey.setText(info.infoKey)
        binding.editModifyInfoValue.setText(info.infoValue)
    }

    override fun getViewBinding() = DialogModifyInfoBinding.inflate(layoutInflater)

}