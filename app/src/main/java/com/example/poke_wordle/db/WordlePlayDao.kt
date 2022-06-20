package com.example.poke_wordle.db

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.poke_wordle.db.model.WordlePlayEntity
import java.time.LocalDate

@Dao
interface WordlePlayDao {
    @Query("SELECT * FROM `pokewordle-play` WHERE date = :date")
    suspend fun getWordlePlay(date: LocalDate): WordlePlayEntity?

    @Query("UPDATE `pokewordle-play` SET attemptsState=:newAttempts, attempts= attempts + 1 WHERE date = :date")
    suspend fun updateWordlePlayGuesses(newAttempts: MutableList<String>, date: LocalDate)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertWordlePlay(play: WordlePlayEntity)

    suspend fun upsertWordlePlay(play: WordlePlayEntity) {
        try {
            insertWordlePlay(play)
        }
        catch (e: SQLiteConstraintException) {
            updateWordlePlayGuesses(play.attemptsState, play.date)
        }
    }

    // TODO("ADD QUERY TO RETURN PLAY STATS")
    // Total jugadas: (SQL COUNT)
    // Porcentaje de Victorias
    // Porcentaje de intentos por jugada
    // Racha actual y Mejor Racha (SQL GAPS AND ISLANDS)
}
