package com.example.poke_wordle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poke_wordle.domain.PokeWordle
import com.example.poke_wordle.domain.Pokemon
import com.example.poke_wordle.repository.PokeWordlePlayRepository
import com.example.poke_wordle.repository.PokemonRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

class PokeWordleViewModel(
    private val pokemonRepository: PokemonRepository,
    private val pokeWordlePlayRepository: PokeWordlePlayRepository
) : ViewModel() {
    init {
        viewModelScope.launch {
            val wordleFromDB = pokeWordlePlayRepository.get(LocalDate.now())
            _wordle.value = wordleFromDB
            fetchPokemonOfTheDay(wordleFromDB)
        }
    }

    private val _pokemonOfTheDay = MutableLiveData<Pokemon>()
    val pokemonOfTheDay: LiveData<Pokemon> = _pokemonOfTheDay

    private val _wordle = MutableLiveData<PokeWordle?>()
    val wordle: LiveData<PokeWordle?> = _wordle

    private val _currentGuess = MutableLiveData("")
    val currentGuess: LiveData<String> = _currentGuess

    fun addGuess() {
        viewModelScope.launch {
            if (currentGuess.value?.length == wordle.value?.solutionWord?.length) {
                currentGuess.value?.let { pokeWordlePlayRepository.updateGuesses(it) }
            }
        }
    }

    fun addLetter(letter: Char) {
        if (_currentGuess.value?.length!! < _wordle.value?.solutionWord?.length!!) {
            _currentGuess.value = currentGuess.value + letter
        }
    }

    fun removeLastLetter() {
        if (_currentGuess.value?.isNotEmpty()!!) {
            _currentGuess.value = currentGuess.value?.dropLast(1)
        }
    }

    private suspend fun fetchPokemonOfTheDay(wordleFromDB: PokeWordle?) {
        if (wordleFromDB == null) {
            _pokemonOfTheDay.value = pokemonRepository.get(1)
            //_pokemonOfTheDay.value = pokemonRepository.getRandomFromDB()
        } else {
            _pokemonOfTheDay.value = pokemonRepository.get(1)
            //_pokemonOfTheDay.value = pokemonRepository.get(wordleFromDB.pokemonId)
        }
    }

    fun createNewGame(level: String) {
        viewModelScope.launch {
            //val pokemonId = _pokemonOfTheDay.value!!.id
            //val pokemonName = _pokemonOfTheDay.value!!.name
            val pokemonId = 1
            val pokemonName = "bulbasaur"
            pokeWordlePlayRepository.newGame(level, pokemonId, pokemonName)
        }
    }
}