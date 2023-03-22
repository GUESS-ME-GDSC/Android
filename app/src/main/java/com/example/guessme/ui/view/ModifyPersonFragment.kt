package com.example.guessme.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.guessme.R
import com.example.guessme.common.base.BaseFragment
import com.example.guessme.databinding.FragmentAddModifyPersonBinding

class ModifyPersonFragment: BaseFragment<FragmentAddModifyPersonBinding>(R.layout.fragment_add_modify_person) {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddModifyPersonBinding {
        return FragmentAddModifyPersonBinding.inflate(inflater, container, false)
    }

}