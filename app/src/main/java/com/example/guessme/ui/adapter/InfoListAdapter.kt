package com.example.guessme.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.guessme.R
import com.example.guessme.data.model.Info
import com.example.guessme.databinding.ItemPersonInfoPreviewBinding
import com.squareup.moshi.internal.Util

class InfoListAdapter: ListAdapter<Info, InfoHolder>(diffCallback) {
    private var delete: Boolean = false
    private var _deleteSet = mutableSetOf<Int>()
    val deleteSet: Set<Int> = _deleteSet

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

    private var onItemClickListener: ((Info) -> Unit)? = null
    fun setOnItemClickListener(listener: (Info) -> Unit) {
        onItemClickListener = listener
    }

    fun setDelete(value: Boolean) {
        delete = value
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