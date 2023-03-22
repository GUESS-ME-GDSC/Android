package com.example.guessme.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.guessme.data.model.Info
import com.example.guessme.databinding.ItemPersonInfoPreviewBinding

class InfoHolder(private val binding: ItemPersonInfoPreviewBinding):
    RecyclerView.ViewHolder(binding.root) {

        fun bind(info: Info) {
            val key = info.infoKey
            val value = info.infoValue

            binding.txtInfoKey.text = key
            binding.txtInfoValue.text = value
        }
}