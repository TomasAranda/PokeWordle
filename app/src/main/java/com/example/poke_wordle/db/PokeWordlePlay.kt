package com.example.poke_wordle.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "pokewordle-play")
data class PokeWordlePlay (
    @PrimaryKey
    val date: Calendar = Calendar.getInstance(),
    val attempts: Int,
    val level: String
)
