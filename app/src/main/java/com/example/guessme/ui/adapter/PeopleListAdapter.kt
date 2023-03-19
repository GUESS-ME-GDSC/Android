package com.example.guessme.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.guessme.data.model.Person
import com.example.guessme.databinding.ItemPersonPreviewBinding

class PeopleListAdapter: ListAdapter<Person, PersonHolder>(diffCallback) {
    private var onItemClickListener: ((Person) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonHolder {
        return PersonHolder(
            ItemPersonPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PersonHolder, position: Int) {
        val person = currentList[position]
        holder.bind(person)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(person) }
        }
    }

    fun setOnItemClickListener(listener: (Person) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        private val diffCallback = object: DiffUtil.ItemCallback<Person>() {
            override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
                return oldItem.uuid == oldItem.uuid
            }

            override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
                return oldItem == newItem
            }

        }
    }

}