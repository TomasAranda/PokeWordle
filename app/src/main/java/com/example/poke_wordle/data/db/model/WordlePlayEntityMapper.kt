package com.example.poke_wordle.data.db.model

import com.example.poke_wordle.domain.PokeWordle

class WordlePlayEntityMapper {
    fun mapToDomainModel(model: WordlePlayEntity?): PokeWordle? {
        return if (model != null) {
            PokeWordle(
                level = model.level,
                solutionWord = model.solutionWord,
                pokemonId = model.pokemonId,
                attempts = model.attempts,
                attemptsState = model.attemptsState,
                currentGuess = mutableListOf(),
                hasWon = model.hasWon
            )
        } else {
            null
        }
    }
}