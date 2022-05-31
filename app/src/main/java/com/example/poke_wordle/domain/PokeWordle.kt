package com.example.poke_wordle.domain

data class PokeWordle (
    val solutionWord: String,
    var attempts: List<MutableList<Char>> = List(6) { MutableList(solutionWord.length) { ' ' } }
)