package com.example.guessme.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.guessme.data.model.Person
import com.example.guessme.data.response.PersonPreview
import com.example.guessme.databinding.ItemPersonPreviewBinding

class PeopleListAdapter: ListAdapter<PersonPreview, PersonHolder>(diffCallback) {
    private var onItemClickListener: ((PersonPreview) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonHolder {
        return PersonHolder(
            ItemPersonPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            parent.context
        )
    }

    override fun onBindViewHolder(holder: PersonHolder, position: Int) {
        val person = currentList[position]
        holder.bind(person)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(person) }
        }
    }

    fun setOnItemClickListener(listener: (PersonPreview) -> Unit) {
        onItemClickListener = listener
    }

    companion object {

        private val diffCallback = object: DiffUtil.ItemCallback<PersonPreview>() {
            override fun areItemsTheSame(oldItem: PersonPreview, newItem: PersonPreview): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: PersonPreview,
                newItem: PersonPreview
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}