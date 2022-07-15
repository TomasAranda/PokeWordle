package com.example.poke_wordle.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import com.example.poke_wordle.domain.PokeWordle
import com.example.poke_wordle.domain.Pokemon
import com.example.poke_wordle.data.repository.PokeWordlePlayRepository
import com.example.poke_wordle.data.repository.PokemonRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

class PokeWordleViewModel(
    private val pokemonRepository: PokemonRepository,
    private val pokeWordlePlayRepository: PokeWordlePlayRepository
) : ViewModel() {

    private val _pokemonOfTheDay = MutableLiveData<Pokemon>()
    val pokemonOfTheDay: LiveData<Pokemon> = _pokemonOfTheDay

    lateinit var wordle: LiveData<PokeWordle?>

    private val _currentGuess = MutableLiveData("")
    val currentGuess: LiveData<String> = _currentGuess

    init {
        viewModelScope.launch {
            val wordleFlow = pokeWordlePlayRepository.get()
            wordle = wordleFlow.asLiveData()
            wordleFlow.collect {
                fetchPokemonOfTheDay(it)
            }
        }
    }

    fun addGuess() {
        val newGuess = currentGuess.value
        if (newGuess?.length == wordle.value?.solutionWord?.length) {
            _currentGuess.value = ""
            viewModelScope.launch {
                if (newGuess == wordle.value?.solutionWord) {
                    pokeWordlePlayRepository.setWin()
                }
                newGuess?.let { pokeWordlePlayRepository.updateGuesses(it) }
            }
        }
    }

    fun addLetter(letter: Char) {
        if (wordle.value?.hasWon == false) {
            if (_currentGuess.value?.length!! < wordle.value?.solutionWord?.length!!) {
                _currentGuess.value = currentGuess.value + letter
            }
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
        }
    }
}