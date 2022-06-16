package com.example.poke_wordle.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.poke_wordle.domain.Pokemon
import com.example.poke_wordle.repository.PokemonRepository

class HintImageViewModel(
    private val pokemonOfTheDay: Pokemon,
    private val pokemonRepository: PokemonRepository
): ViewModel() {

    fun getPokemonImageUrl(): String {
        Log.d("IMAGE HINT DIALOG", pokemonOfTheDay.toString())
        return pokemonOfTheDay.imageUrl
    }

    fun getPokemonType(): List<String> {
        return pokemonOfTheDay.types
    }
}