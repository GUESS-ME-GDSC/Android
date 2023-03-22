package com.example.guessme.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.guessme.data.model.Info
import com.example.guessme.databinding.ItemPersonInfoModifyBinding

class InfoModifyHolder(private val binding: ItemPersonInfoModifyBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(info: Info) {
        binding.txtInfoKey.text = info.infoKey
        binding.editInfoValue.setText(info.infoValue)
    }
}