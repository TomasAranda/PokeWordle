package com.example.poke_wordle.ui.viewmodel

import androidx.lifecycle.*
import com.example.poke_wordle.data.repository.PokeWordlePlayRepository
import com.example.poke_wordle.data.repository.PokemonRepository
import kotlinx.coroutines.launch

class LevelPickerViewModel(
    private val pokemonRepository: PokemonRepository,
    private val pokeWordlePlayRepository: PokeWordlePlayRepository
): ViewModel() {
    fun createNewGame(level: String) {
        viewModelScope.launch {
            pokemonRepository.getRandomFromDB().let {
                pokeWordlePlayRepository.newGame(level, it.id, it.name)
            }
        }
    }
}