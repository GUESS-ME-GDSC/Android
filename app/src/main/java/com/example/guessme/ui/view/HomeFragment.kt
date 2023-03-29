package com.example.guessme.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.guessme.R
import com.example.guessme.common.base.BaseFragment
import com.example.guessme.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        binding.btnHomePeopleList.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_home_to_peopleListFragment)
        }

        binding.btnHomeQuiz.setOnClickListener {
            val action = HomeFragmentDirections.actionFragmentHomeToStartQuizFragment(0)
            findNavController().navigate(action)
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity)!!.supportActionBar!!.show()
    }

}