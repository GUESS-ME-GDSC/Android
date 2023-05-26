package com.example.guessme.ui.view

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.guessme.R
import com.example.guessme.common.base.BaseFragment
import com.example.guessme.data.model.User
import com.example.guessme.databinding.FragmentSignUpBinding
import com.example.guessme.ui.dialog.NoticeDialog
import com.example.guessme.ui.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {
    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSignUpBinding {
        return FragmentSignUpBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUpCancel.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_fragment_login)
        }

        binding.btnSignUp.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val id = binding.editSignUpId.text.toString()
                val pw = binding.editSignUpPw.text.toString()
                val email = binding.editSignUpEmail.text.toString()

                signUp(User(null, id, pw, email))
            }
        }

        signUpViewModel.isSignUp.observe(viewLifecycleOwner) { isSignUp ->
            if (isSignUp) {
                val dialog = NoticeDialog(R.string.dialog_sign_up_success)
                dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")

                object : CountDownTimer(3000, 1000) {
                    override fun onTick(p0: Long) {
                    }

                    override fun onFinish() {
                        dialog.dismiss()
                    }
                }.start()

                findNavController().navigate(R.id.action_signUpFragment_to_fragment_login)
            }
        }

        signUpViewModel.errorState.observe(viewLifecycleOwner) { error ->
            val dialog = NoticeDialog(error)
            dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
        }
    }

    private suspend fun signUp(user: User) {
        try {
            signUpViewModel.signUp(user)
        } catch (e: Exception) {
            val dialog = NoticeDialog(R.string.dialog_msg_error)
            dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
        }
    }

}