package com.example.poke_wordle.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poke_wordle.repository.PokeWordlePlayRepository
import com.example.poke_wordle.repository.PokemonRepository
import kotlinx.coroutines.launch

class LevelPickerViewModel(
    private val pokemonRepository: PokemonRepository,
    private val pokeWordlePlayRepository: PokeWordlePlayRepository
): ViewModel() {
    fun createNewGame(level: String) {
        viewModelScope.launch {
            val pokemonId = pokemonRepository.getRandomFromDB().id
            pokeWordlePlayRepository.newGame(level, pokemonId)
        }
    }
}