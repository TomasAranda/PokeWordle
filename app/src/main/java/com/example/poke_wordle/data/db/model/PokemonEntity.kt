package com.example.poke_wordle.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey @ColumnInfo(name = "id")
    val id: Int,
    val name: String,
    val url: String
)
