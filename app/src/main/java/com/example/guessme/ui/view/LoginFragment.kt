package com.example.guessme.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.guessme.R
import com.example.guessme.common.base.BaseFragment
import com.example.guessme.data.model.User
import com.example.guessme.databinding.FragmentLoginBinding
import com.example.guessme.ui.dialog.NoticeDialog
import com.example.guessme.ui.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

@AndroidEntryPoint
class LoginFragment: BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    private val logInViewModel: LoginViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logInViewModel.getToken()

        binding.btnLoginSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_login_to_signUpFragment)
        }

        binding.btnLogin.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val id = binding.editLoginId.text.toString()
                val pw = binding.editLoginPw.text.toString()

                logIn(User(null, id, pw, null))
            }
        }

        logInViewModel.isLogin.observe(viewLifecycleOwner) { isLogIn ->
            if (isLogIn) {
                findNavController().navigate(R.id.action_fragment_login_to_homeFragment)
            }
        }

        logInViewModel.errorMsg.observe(viewLifecycleOwner) { msg ->
            if (msg == 400) {
                val dialog = NoticeDialog(R.string.login_error)
                dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
            }
        }

        logInViewModel.errorState.observe(viewLifecycleOwner) { error ->
            if (error) {
                val dialog = NoticeDialog(R.string.dialog_msg_error)
                dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
            }
        }

        logInViewModel.token.observe(viewLifecycleOwner) { token ->
            if (! token.isNullOrEmpty()) {
                findNavController().navigate(R.id.action_fragment_login_to_homeFragment)
            }
        }
    }

    private suspend fun logIn(user: User) {
        try {
            logInViewModel.login(user)
        }catch (e: Exception) {
            val dialog = NoticeDialog(R.string.dialog_permission)
            dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

}