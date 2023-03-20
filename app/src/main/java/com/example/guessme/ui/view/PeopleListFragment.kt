package com.example.guessme.ui.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guessme.R
import com.example.guessme.common.base.BaseFragment
import com.example.guessme.data.model.Person
import com.example.guessme.databinding.FragmentPeopleListBinding
import com.example.guessme.ui.adapter.PeopleListAdapter
import java.time.LocalDateTime

class PeopleListFragment : BaseFragment<FragmentPeopleListBinding>(R.layout.fragment_people_list) {
    private lateinit var peopleListAdapter: PeopleListAdapter

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPeopleListBinding {
        return FragmentPeopleListBinding.inflate(inflater, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        peopleListAdapter.submitList(getTempPeopleData())

        binding.fabListAddPerson.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_people_list_to_fragment_add_person)
        }

    }

    private fun setupRecyclerView() {
        peopleListAdapter = PeopleListAdapter()
        binding.recyclerPeopleList.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = peopleListAdapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTempPeopleData(): ArrayList<Person> {
        val data = ArrayList<Person>()

        data.add(Person(0, true, null, null, null, "길동이", "손자", LocalDateTime.now(), "경기도 처인구", 30))
        data.add(Person(0, false, null, null, null, "동백이", "손녀", LocalDateTime.now(), "경기도 처인구", 60))

        return data
    }
}