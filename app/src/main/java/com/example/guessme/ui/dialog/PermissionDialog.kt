package com.example.guessme.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.guessme.R
import com.example.guessme.common.base.BaseDialog
import com.example.guessme.databinding.DialogBaseBinding

class PermissionDialog: BaseDialog<DialogBaseBinding>(R.layout.dialog_base) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding.txtPermission.setText(R.string.dialog_permission)
        binding.btnPermission.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun getViewBinding() = DialogBaseBinding.inflate(layoutInflater)

}