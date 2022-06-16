package com.example.poke_wordle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poke_wordle.domain.Pokemon
import com.example.poke_wordle.repository.PokeWordlePlayRepository
import com.example.poke_wordle.repository.PokemonRepository
import kotlinx.coroutines.launch

class LevelPickerViewModel(
    private val pokemonRepository: PokemonRepository,
    private val pokeWordlePlayRepository: PokeWordlePlayRepository
): ViewModel() {
    private val selectedPokemon = MutableLiveData<Pokemon>().also {
        viewModelScope.launch {
            it.value = pokemonRepository.getRandomFromDB()
        }
    }

    fun createNewGame(level: String) {
        viewModelScope.launch {
            val pokemonName = pokemonRepository.get(selectedPokemon.value?.id!!).name
            pokeWordlePlayRepository.newGame(level, selectedPokemon.value?.id!!, pokemonName)
        }
    }
}