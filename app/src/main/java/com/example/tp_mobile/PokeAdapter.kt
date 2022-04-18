package com.example.tp_mobile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class PokeAdapter( private val pokemon:List<String>):RecyclerView.Adapter<PokeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokeViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        return PokeViewHolder(layoutInflater.inflate(R.layout.item_poke,parent,false))
    }

    override fun onBindViewHolder(holder: PokeViewHolder, position: Int) {
        val item = pokemon[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = pokemon.size
}