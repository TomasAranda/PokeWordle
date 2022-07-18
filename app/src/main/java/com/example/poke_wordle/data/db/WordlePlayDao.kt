package com.example.poke_wordle.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.poke_wordle.data.db.model.WordlePlayEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface WordlePlayDao {
    @Query("SELECT * FROM `pokewordle-play` WHERE date = :date")
    suspend fun getWordlePlay(date: LocalDate): WordlePlayEntity?

    @Query("SELECT * FROM `pokewordle-play` WHERE date = :date")
    fun getObservableWordlePlay(date: LocalDate): Flow<WordlePlayEntity?>

    @Query("UPDATE `pokewordle-play` SET hasWon=1 WHERE date = :date")
    suspend fun updateWordlePlayWinningState(date: LocalDate)

    @Query("UPDATE `pokewordle-play` SET attemptsState=:newAttempts, attempts= attempts + 1 WHERE date = :date")
    suspend fun updateWordlePlayGuesses(newAttempts: List<String>, date: LocalDate)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertWordlePlay(play: WordlePlayEntity)

    // STATS
    @Query("SELECT COUNT(*) FROM `pokewordle-play`")
    suspend fun getWordlePlayCount(): Int

    @Query("SELECT COUNT(*) FROM `pokewordle-play` WHERE hasWon=1")
    suspend fun getWordlePlayWinsCount(): Int

    @Query("SELECT COUNT(*) FROM `pokewordle-play` WHERE attempts=:attempts AND hasWon=1")
    suspend fun getWonPlaysCountByAttempts(attempts: Int): Int
}
