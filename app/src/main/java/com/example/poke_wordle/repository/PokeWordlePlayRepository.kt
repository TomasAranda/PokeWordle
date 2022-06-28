package com.example.poke_wordle.repository

import com.example.poke_wordle.db.WordlePlayDao
import com.example.poke_wordle.db.model.WordlePlayEntity
import com.example.poke_wordle.db.model.toDomainModel
import com.example.poke_wordle.domain.PokeWordle
import java.time.LocalDate

class PokeWordlePlayRepository(
    private val wordlePlayDao: WordlePlayDao
) {
    suspend fun newGame(level: String, pokemonId: Int, pokemonName: String) {
        val newGame = WordlePlayEntity(
            LocalDate.now(),
            pokemonId,
            pokemonName,
            0,
            MutableList(6) { "" },
            false,
            level)
        wordlePlayDao.insertWordlePlay(newGame)
    }

    suspend fun get(date: LocalDate): PokeWordle? {
        return wordlePlayDao.getWordlePlay(date)?.toDomainModel()
    }

    suspend fun updateGuesses(newGuess: String) {
        val currentPlay = wordlePlayDao.getWordlePlay(LocalDate.now())
        val newGuesses = currentPlay?.attemptsState?.mapIndexed { index, guess ->
            if (index == currentPlay.attempts) newGuess else guess
        }
        newGuesses?.let { wordlePlayDao.updateWordlePlayGuesses(it, LocalDate.now()) }
    }

}