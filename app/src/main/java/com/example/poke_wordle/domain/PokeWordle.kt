package com.example.poke_wordle.domain

data class PokeWordle (
    val solutionWord: String,
    val pokemonId: Int,
    var attempts: List<MutableList<Char>> = List(6) { MutableList(solutionWord.length) { ' ' } }
)