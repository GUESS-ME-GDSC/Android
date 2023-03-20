package com.example.guessme.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.guessme.R
import com.example.guessme.common.base.BaseFragment
import com.example.guessme.databinding.FragmentAddPersonBinding

class AddPersonFragment : BaseFragment<FragmentAddPersonBinding>(R.layout.fragment_add_person) {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddPersonBinding {
        return FragmentAddPersonBinding.inflate(inflater, container, false)
    }
}