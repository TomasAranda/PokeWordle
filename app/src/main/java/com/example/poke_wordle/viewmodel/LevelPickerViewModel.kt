package com.example.poke_wordle.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.poke_wordle.domain.Pokemon
import com.example.poke_wordle.repository.PokeWordlePlayRepository
import com.example.poke_wordle.repository.PokemonRepository
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