package com.example.poke_wordle.domain

data class PokeWordle (
    val level: String,
    val solutionWord: String,
    val pokemonId: Int,
    val attempts: Int,
    var attemptsState: List<String> = List(6) { "" },
    var currentGuess: MutableList<LetterState>
)