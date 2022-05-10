package com.example.tp_mobile.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class Pokemon(
    @PrimaryKey @ColumnInfo(name = "id")
    val pokemonId: Int,
    val name: String,
    val primaryType: String,
    val secondaryType: String,
    val imageUrl: String
)
