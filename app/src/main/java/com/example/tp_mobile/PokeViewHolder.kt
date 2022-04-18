package com.example.tp_mobile

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_mobile.databinding.ItemPokeBinding
import com.squareup.picasso.Picasso

class PokeViewHolder(view: View):RecyclerView.ViewHolder(view) {
    private val binding = ItemPokeBinding.bind(view)
    fun bind(pokemon:String){
        Picasso.get().load(pokemon).into(binding.ivPokemon)
        binding.ivPokemon

    }
}