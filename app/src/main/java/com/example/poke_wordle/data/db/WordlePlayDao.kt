package com.example.poke_wordle.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.poke_wordle.data.db.model.WordlePlayEntity
import java.time.LocalDate

@Dao
interface WordlePlayDao {
    @Query("SELECT * FROM `pokewordle-play` WHERE date = :date")
    suspend fun getWordlePlay(date: LocalDate): WordlePlayEntity?

    @Query("UPDATE `pokewordle-play` SET attemptsState=:newAttempts, attempts= attempts + 1 WHERE date = :date")
    suspend fun updateWordlePlayGuesses(newAttempts: List<String>, date: LocalDate)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertWordlePlay(play: WordlePlayEntity)

    // TODO("ADD QUERY TO RETURN PLAY STATS")
    // Total jugadas: (SQL COUNT)
    // Porcentaje de Victorias
    // Porcentaje de intentos por jugada
    // Racha actual y Mejor Racha (SQL GAPS AND ISLANDS)
}
