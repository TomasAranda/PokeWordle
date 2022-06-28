package com.example.poke_wordle.data.repository

import com.example.poke_wordle.data.db.WordlePlayDao
import com.example.poke_wordle.data.db.model.WordlePlayEntity
import com.example.poke_wordle.data.db.model.toDomainModel
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

    suspend fun get(): PokeWordle? {
        return wordlePlayDao.getWordlePlay(LocalDate.now())?.toDomainModel()
    }

    suspend fun updateGuesses(newGuess: String) {
        val currentPlay = wordlePlayDao.getWordlePlay(LocalDate.now())
        val newGuesses = currentPlay?.attemptsState?.mapIndexed { index, guess ->
            if (index == currentPlay.attempts) newGuess else guess
        }
        newGuesses?.let { wordlePlayDao.updateWordlePlayGuesses(it, LocalDate.now()) }
    }

    suspend fun setWin() {
        wordlePlayDao.updateWordlePlayWinningState(LocalDate.now())
    }

}