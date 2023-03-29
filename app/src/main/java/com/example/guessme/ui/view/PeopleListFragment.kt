package com.example.guessme.ui.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guessme.R
import com.example.guessme.common.base.BaseFragment
import com.example.guessme.data.model.Person
import com.example.guessme.data.response.PersonPreview
import com.example.guessme.databinding.FragmentPeopleListBinding
import com.example.guessme.ui.adapter.PeopleListAdapter
import com.example.guessme.ui.dialog.NoticeDialog
import com.example.guessme.ui.viewmodel.PeopleListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class PeopleListFragment : BaseFragment<FragmentPeopleListBinding>(R.layout.fragment_people_list) {
    private lateinit var peopleListAdapter: PeopleListAdapter
    private val peopleListViewModel: PeopleListViewModel by viewModels()

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
        setObserver()

        CoroutineScope(Dispatchers.IO).launch {
//            peopleListViewModel.getPeopleList(true)
            peopleListViewModel.getPeopleList(false)
        }

        peopleListAdapter.setOnItemClickListener { personPreview ->
            val id = personPreview.id.toInt()
            val action = PeopleListFragmentDirections.actionFragmentPeopleListToFragmentPersonDetail(id)
            findNavController().navigate(action)
        }

        binding.fabListAddPerson.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_people_list_to_fragment_add_person)
        }

    }

    private fun setObserver() {
        var newList: List<PersonPreview>
        peopleListViewModel.favoritePeopleList.observe(viewLifecycleOwner) { favoriteList ->
            newList = favoriteList
            peopleListViewModel.notFavoritePeopleList.value?.let {
                newList += it
            }
            peopleListAdapter.submitList(newList)
        }

        peopleListViewModel.notFavoritePeopleList.observe(viewLifecycleOwner) { notFavoriteList ->
            newList = if (peopleListViewModel.favoritePeopleList.value == null) {
                notFavoriteList
            } else {
                peopleListViewModel.favoritePeopleList.value!! + notFavoriteList
            }

            peopleListAdapter.submitList(newList)
        }

        peopleListViewModel.getPeopleList.observe(viewLifecycleOwner) { getPeopleList ->
            if (! getPeopleList){
                val dialog = NoticeDialog(R.string.dialog_msg_error)
                dialog.show(requireActivity().supportFragmentManager, "NoticeDialog")
            }
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
}