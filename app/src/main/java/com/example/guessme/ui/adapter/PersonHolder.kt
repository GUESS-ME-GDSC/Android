package com.example.guessme.ui.adapter

import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.guessme.common.util.GlideApp
import com.example.guessme.data.response.PersonPreview
import com.example.guessme.databinding.ItemPersonPreviewBinding


class PersonHolder(private val binding: ItemPersonPreviewBinding, private val context: Context):
    RecyclerView.ViewHolder(binding.root) {

        fun bind(person: PersonPreview) {
            val name = person.name
            val relation = person.relation
            val favorite = person.favorite
            val score = person.score
            person.image?.let {
                Log.d("image", it)

                GlideApp.with(context).load(it).into(binding.imagePreviewPerson);
            }

            if (favorite) {
                binding.imagePreviewFavoriteTrue.visibility = View.VISIBLE
            }else {
                binding.imagePreviewFavoriteFalse.visibility = View.VISIBLE
            }

            binding.txtPreviewName.text = name
            binding.txtPreviewRelation.text = relation
            binding.txtPreviewScore.text = "$score%"
            binding.progressPreviewScore.progress = score
        }

}