package com.example.poke_wordle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poke_wordle.db.model.toDomainModel
import com.example.poke_wordle.domain.PokeWordle
import com.example.poke_wordle.domain.Pokemon
import com.example.poke_wordle.repository.PokeWordlePlayRepository
import com.example.poke_wordle.repository.PokemonRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

class WelcomeViewModel(
    private val pokemonRepository: PokemonRepository,
    private val pokeWordlePlayRepository: PokeWordlePlayRepository
): ViewModel() {
    //val pokemonOfTheDay = MutableLiveData<Pokemon>()

    val wordlePlay = MutableLiveData<PokeWordle>().also {
        viewModelScope.launch {
            pokeWordlePlayRepository.get(LocalDate.now())
        }
    }

    /*fun retrievePokemonOfTheDay(wordle: PokeWordle?) {
        viewModelScope.launch {
            wordle?.let {
                pokemonOfTheDay.value = pokemonRepository.get(wordle.pokemonId)
            }
            pokemonOfTheDay.value = pokemonRepository.getRandomFromDB().value
        }
    }*/
}