package com.example.guessme.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.guessme.R
import com.example.guessme.common.base.BaseFragment
import com.example.guessme.databinding.FragmentScoringBinding

class ScoringFragment : BaseFragment<FragmentScoringBinding>(R.layout.fragment_scoring) {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentScoringBinding {
        return FragmentScoringBinding.inflate(inflater, container, false)
    }
}