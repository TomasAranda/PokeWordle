package com.example.poke_wordle.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "pokewordle-play")
data class PokeWordlePlayEntity (
    @PrimaryKey
    val date: Calendar = Calendar.getInstance(),
    val pokemonId: Int,
    val attempts: Int,
    val level: String
)
