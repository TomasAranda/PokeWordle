package com.example.poke_wordle.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
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

class PokeWordleViewModel(
    private val pokemonRepository: PokemonRepository,
    private val pokeWordlePlayRepository: PokeWordlePlayRepository
) : ViewModel() {
    private var nextPosition = mutableListOf(0,0)

    init {
        viewModelScope.launch {
            val wordle = pokeWordlePlayRepository.get(LocalDate.now())
            _wordle.value = wordle
            fetchPokemonOfTheDay(wordle)
        }
    }

    private val _pokemonOfTheDay = MutableLiveData<Pokemon>()
    val pokemonOfTheDay: LiveData<Pokemon> = _pokemonOfTheDay

    private val _wordle = MutableLiveData<PokeWordle?>()
    val wordle: LiveData<PokeWordle?> = _wordle

    fun addGuess() {

    }

    fun addLetter(letter: Char) {
        if (nextPosition.first() <= 5) {
            val updatedAttempts: List<MutableList<Char>> = wordle.value?.attempts ?: List(6) { MutableList(7) { ' ' } }
            updatedAttempts[nextPosition.first()][nextPosition.last()] = letter
            if (nextPosition[1] < wordle.value?.solutionWord?.length!! - 1) {
                nextPosition[1] += 1
            } else {
                nextPosition[0] += 1
                nextPosition[1] = 0
            }
            val newState = wordle.value?.let { PokeWordle(it.level, it.solutionWord, it.pokemonId, updatedAttempts) }
            _wordle.postValue(newState)
            Log.d("WORDLE STATE", wordle.value.toString())
            Log.d("ADD LETTER", nextPosition.toString())
        }
    }

    fun removeLastLetter() {
        if (!(nextPosition.first() == 0 && nextPosition.last() == 0)){
            val updatedAttempts: List<MutableList<Char>> = wordle.value?.attempts ?: List(6) { MutableList(7) { ' ' } }
            val previousPosition: MutableList<Int> = if (nextPosition[1] == 0) {
                mutableListOf(nextPosition[0] - 1, wordle.value?.solutionWord?.length!! - 1)
            } else {
                mutableListOf(nextPosition[0], nextPosition[1] - 1)
            }
            updatedAttempts[previousPosition.first()][previousPosition.last()] = ' '
            nextPosition = previousPosition

            val newState = wordle.value?.let { PokeWordle(it.level, it.solutionWord, it.pokemonId, updatedAttempts) }
            _wordle.postValue(newState)
        }
    }

    suspend fun fetchPokemonOfTheDay(wordle: PokeWordle?) {
        if (wordle == null) {
            _pokemonOfTheDay.value = pokemonRepository.getRandomFromDB()
        } else {
            _pokemonOfTheDay.value = pokemonRepository.get(wordle.pokemonId)
        }
    }

    fun createNewGame(level: String) {
        viewModelScope.launch {
            val pokemonId = pokemonOfTheDay.value!!.id
            val pokemonName = _pokemonOfTheDay.value!!.name
            pokeWordlePlayRepository.newGame(level, pokemonId, pokemonName)
        }
    }
}