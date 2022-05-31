package com.example.poke_wordle.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.poke_wordle.domain.PokeWordle

class PokeWordleViewModel : ViewModel() {
    // var lastPosition ??
    // var nextPosition ??

    val wordle = MutableLiveData<PokeWordle?>()

    fun new() {
        wordle.postValue(
            PokeWordle(
            "pikachu"
        )
        )
    }

    fun addLetter(letter: Char) {
        val updatedAttempts: List<MutableList<Char>> = wordle.value?.attempts ?: List(6) { MutableList(7) { ' ' } }
        val nextPosition = wordle.value?.let { getNextPosition(it.attempts) }
        if (nextPosition != null) {
            updatedAttempts[nextPosition.first()][nextPosition.last()] = letter
        }

        val newState = wordle.value?.let { PokeWordle(it.solutionWord, updatedAttempts) }
        wordle.postValue(newState)
    }

    fun removeLastLetter() {
        val updatedAttempts: List<MutableList<Char>> = wordle.value?.attempts ?: List(6) { MutableList(7) { ' ' } }
        val nextPosition = wordle.value?.let { getLastPosition(it.attempts) }
        if (nextPosition != null) {
            updatedAttempts[nextPosition.first()][nextPosition.last()] = ' '
        }

        val newState = wordle.value?.let { PokeWordle(it.solutionWord, updatedAttempts) }
        wordle.postValue(newState)
    }

    private fun getNextPosition(attempts: List<List<Char>>): List<Int> {
        for ((rowIndex, row) in attempts.withIndex()) {
            for ((charIndex, char) in row.withIndex()) {
                if (char == ' ') {
                    return listOf(rowIndex, charIndex)
                }
            }
        }
        return listOf(0, 0)
    }

    private fun getLastPosition(attempts: List<List<Char>>): List<Int>? {
        var lastPosition: List<Int>? = null
        for ((rowIndex, attempt) in attempts.withIndex()) {
            for ((charIndex, char) in attempt.withIndex()) {
                if (char != ' ') {
                    lastPosition = listOf(rowIndex, charIndex)
                } else {
                    return lastPosition
                }
            }
        }
        return lastPosition
    }
}