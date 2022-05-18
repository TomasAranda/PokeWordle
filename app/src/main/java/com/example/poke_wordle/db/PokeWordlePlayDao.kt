package com.example.poke_wordle.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.Calendar

@Dao
interface PokeWordlePlayDao {
    @Query("SELECT * FROM `pokewordle-play` WHERE date = :date")
    fun getPokeWordlePlay(date: Calendar): PokeWordlePlay?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(play: PokeWordlePlay)

    // TODO("ADD QUERY TO RETURN PLAY STATS")
    // Total jugadas: (SQL COUNT)
    // Porcentaje de Victorias
    // Porcentaje de intentos por jugada
    // Racha actual y Mejor Racha (SQL GAPS AND ISLANDS)
}
