package com.example.tp_mobile.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon-from-list")
data class PokemonFromList(
    @PrimaryKey @ColumnInfo(name = "id")
    val id: Int,
    val name: String,
    val url: String
)