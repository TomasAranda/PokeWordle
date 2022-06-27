package com.example.poke_wordle.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poke_wordle.domain.PokeWordle
import com.example.poke_wordle.domain.Pokemon
import com.example.poke_wordle.data.repository.PokeWordlePlayRepository
import com.example.poke_wordle.data.repository.PokemonRepository
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
            _currentGuessNumber.value = wordleFromDB?.attempts?.plus(1) ?: 1
            fetchPokemonOfTheDay(wordleFromDB)
        }
    }

    private val _pokemonOfTheDay = MutableLiveData<Pokemon>()
    val pokemonOfTheDay: LiveData<Pokemon> = _pokemonOfTheDay

    private val _wordle = MutableLiveData<PokeWordle?>()
    val wordle: LiveData<PokeWordle?> = _wordle

    private val _currentGuess = MutableLiveData("")
    val currentGuess: LiveData<String> = _currentGuess

    private val _currentGuessNumber = MutableLiveData<Int>()
    val currentGuessNumber: LiveData<Int> = _currentGuessNumber

    fun addGuess() {
        viewModelScope.launch {
            if (currentGuess.value?.length == wordle.value?.solutionWord?.length) {
                currentGuess.value?.let {
                    pokeWordlePlayRepository.updateGuesses(it)
                    _wordle.value = pokeWordlePlayRepository.get(LocalDate.now())
                    _currentGuessNumber.value = _currentGuessNumber.value?.plus(1)
                }
                _currentGuess.value = ""
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
            _pokemonOfTheDay.value = pokemonRepository.getRandomFromDB()
        } else {
            _pokemonOfTheDay.value = pokemonRepository.get(wordleFromDB.pokemonId)
        }
    }

    fun createNewGame(level: String) {
        viewModelScope.launch {
            val pokemonId = _pokemonOfTheDay.value!!.id
            val pokemonName = _pokemonOfTheDay.value!!.name
            pokeWordlePlayRepository.newGame(level, pokemonId, pokemonName)
            _wordle.value = pokeWordlePlayRepository.get(LocalDate.now())
        }
    }
}