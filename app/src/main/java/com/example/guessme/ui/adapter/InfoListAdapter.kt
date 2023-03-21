package com.example.guessme.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.guessme.data.model.Info
import com.example.guessme.databinding.ItemPersonInfoPreviewBinding
import com.squareup.moshi.internal.Util

class InfoListAdapter: ListAdapter<Info, InfoHolder>(diffCallback) {
    private var onItemClickListener: ((Info) -> Util)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoHolder {
        return InfoHolder(
            ItemPersonInfoPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: InfoHolder, position: Int) {
        val info = currentList[position]
        holder.bind(info)

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(info) }
        }

    }

    fun setOnItemClickListener(listener: (Info) -> Util) {
        onItemClickListener = listener
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