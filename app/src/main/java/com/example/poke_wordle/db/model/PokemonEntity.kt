package com.example.poke_wordle.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.poke_wordle.domain.Pokemon

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey @ColumnInfo(name = "id")
    val id: Int,
    val name: String,
    val url: String
)

internal fun PokemonEntity.toDomainModel() = Pokemon(
    this.id,
    this.name,
    this.url,
    listOf()
)
