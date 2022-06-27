package com.example.poke_wordle.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.poke_wordle.data.repository.PokemonRepository

class HintImageViewModel(
    private val pokemonRepository: PokemonRepository
): ViewModel() {

    fun getPokemonImageUrl(): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/132.png"
    }

    fun getPokemonType(): List<String> {
        return listOf("fire")
    }
}