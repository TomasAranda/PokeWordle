package com.example.poke_wordle.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import com.example.poke_wordle.data.db.model.PokemonEntity


@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemon: List<PokemonEntity>)

    @Query("SELECT * FROM `pokemon` ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomPokemonFromList(): PokemonEntity

    @Delete
    suspend fun delete(pokemon: PokemonEntity): Int

    suspend fun selectRandomPokemon(): PokemonEntity {
        val selectedPokemon = getRandomPokemonFromList()
        delete(selectedPokemon)
        return selectedPokemon
    }
}