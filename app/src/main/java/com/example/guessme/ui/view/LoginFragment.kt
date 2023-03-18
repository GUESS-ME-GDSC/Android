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
import com.example.guessme.databinding.FragmentLoginBinding

class LoginFragment: BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLoginSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_login_to_signUpFragment)
        }

        binding.btnLogin.setOnClickListener {
            //user 정보 전달 필요
            findNavController().navigate(R.id.action_fragment_login_to_homeFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

}