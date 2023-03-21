package com.example.guessme.ui.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.guessme.R
import com.example.guessme.common.base.BaseFragment
import com.example.guessme.databinding.FragmentPersonDetailBinding
import com.example.guessme.ui.viewmodel.PersonDetailViewModel
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

class PersonDetailFragment : BaseFragment<FragmentPersonDetailBinding>(R.layout.fragment_person_detail) {
    private val args: PersonDetailFragmentArgs by navArgs<PersonDetailFragmentArgs>()
    private val personDetailViewModel by viewModels<PersonDetailViewModel>()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPersonDetailBinding {
        return FragmentPersonDetailBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        personDetailViewModel.setPerson(args.person)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        personDetailViewModel.person?.let {
            val dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd")
            binding.txtDetailName.text = it.name
            binding.txtDetailRelation.text = it.relation
            binding.txtDetailBirth.text = it.birth.format(dateFormat)
            binding.txtDetailAddress.text = it.residence
        }
    }

}