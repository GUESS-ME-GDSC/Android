package com.example.guessme.ui.adapter

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.guessme.data.model.Person
import com.example.guessme.data.response.PersonPreview
import com.example.guessme.databinding.ItemPersonPreviewBinding

class PersonHolder(private val binding: ItemPersonPreviewBinding):
    RecyclerView.ViewHolder(binding.root) {

        fun bind(person: PersonPreview) {
            val name = person.name
            val relation = person.relation
            val favorite = person.favorite
            val score = person.score
            val imageUri = Uri.parse(person.image)

            if (favorite) {
                binding.imagePreviewFavoriteTrue.visibility = View.VISIBLE
            }else {
                binding.imagePreviewFavoriteFalse.visibility = View.VISIBLE
            }

            binding.txtPreviewName.text = name
            binding.txtPreviewRelation.text = relation
            binding.txtPreviewScore.text = "$score%"
            binding.progressPreviewScore.progress = score!!
            binding.imagePreviewPerson.setImageURI(imageUri)
        }
}