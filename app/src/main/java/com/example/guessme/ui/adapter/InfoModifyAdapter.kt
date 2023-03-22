package com.example.guessme.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.guessme.data.model.Info
import com.example.guessme.databinding.ItemPersonInfoModifyBinding

class InfoModifyAdapter: ListAdapter<Info, InfoModifyHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoModifyHolder {
        return InfoModifyHolder(
            ItemPersonInfoModifyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: InfoModifyHolder, position: Int) {
        val info = currentList[position]
        holder.bind(info)
    }

    companion object {
        private val diffCallback = object: DiffUtil.ItemCallback<Info>() {
            override fun areItemsTheSame(oldItem: Info, newItem: Info): Boolean {
                return oldItem.uuid == newItem.uuid
            }

            override fun areContentsTheSame(oldItem: Info, newItem: Info): Boolean {
                return oldItem == newItem
            }

        }
    }

}