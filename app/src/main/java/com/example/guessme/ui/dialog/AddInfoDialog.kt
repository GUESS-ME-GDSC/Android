package com.example.guessme.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.guessme.R
import com.example.guessme.common.base.BaseDialog
import com.example.guessme.databinding.DialogAddInfoBinding

class AddInfoDialog: BaseDialog<DialogAddInfoBinding>(R.layout.dialog_add_info) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.btnAddInfoCancel.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun getViewBinding() = DialogAddInfoBinding.inflate(layoutInflater)

}