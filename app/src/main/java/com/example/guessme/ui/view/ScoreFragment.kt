package com.example.guessme.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.guessme.R
import com.example.guessme.common.base.BaseFragment
import com.example.guessme.databinding.FragmentScoreBinding

class ScoreFragment : BaseFragment<FragmentScoreBinding>(R.layout.fragment_score) {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentScoreBinding {
        return FragmentScoreBinding.inflate(inflater, container, false)
    }
}