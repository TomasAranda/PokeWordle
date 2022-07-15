package com.example.poke_wordle.data.repository

import com.example.poke_wordle.data.db.WordlePlayDao
import com.example.poke_wordle.data.db.model.WordlePlayEntity
import com.example.poke_wordle.data.db.model.WordlePlayEntityMapper
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import kotlin.math.roundToInt

class PokeWordlePlayRepository(
    private val wordlePlayDao: WordlePlayDao,
    private val mapper: WordlePlayEntityMapper
) {
    suspend fun newGame(level: String, pokemonId: Int, pokemonName: String) {
        val newGame = WordlePlayEntity(
            LocalDate.now(),
            pokemonId,
            pokemonName,
            0,
            MutableList(6) { "" },
            false,
            level
        )
        wordlePlayDao.insertWordlePlay(newGame)
    }

    fun get() = wordlePlayDao.getObservableWordlePlay(LocalDate.now()).map { mapper.mapToDomainModel(it) }

    suspend fun getCount(): Int {
        return wordlePlayDao.getWordlePlayCount()
    }

    suspend fun getWinPercentage(): Double {
        val total = wordlePlayDao.getWordlePlayCount()
        val totalWins = wordlePlayDao.getWordlePlayWinsCount()
        return if (total != 0) {
            ((totalWins / total.toDouble()) * 1000.0).roundToInt().toDouble() / 10
        } else {
            0.0
        }
    }

    suspend fun getWinPercentagesByAttempts(level: String = ""): List<Double> {
        val percentagesByAttempts = MutableList(6) { 0.0 }
        val totalCount = wordlePlayDao.getWordlePlayCount()
        if (totalCount > 0) {
            for (attempt in 1..6) {
                val countByAttempt = wordlePlayDao.getWonPlaysCountByAttempts(attempt)
                val roundedPercentage = ((countByAttempt / totalCount.toDouble()) * 1000.0).roundToInt().toDouble() / 10
                percentagesByAttempts[attempt-1] = roundedPercentage
            }
        }
        return percentagesByAttempts.toList()
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