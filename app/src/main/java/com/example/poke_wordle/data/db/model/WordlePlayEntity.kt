package com.example.poke_wordle.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.poke_wordle.domain.PokeWordle
import java.time.LocalDate

@Entity(tableName = "pokewordle-play")
data class WordlePlayEntity (
    @PrimaryKey
    val date: LocalDate,
    val pokemonId: Int,
    val solutionWord: String,
    var attempts: Int,
    var attemptsState: List<String> = List(6) { "" },
    var hasWon: Boolean,
    val level: String
)

internal fun WordlePlayEntity.toDomainModel(): PokeWordle {
    return PokeWordle(
        this.level,
        this.solutionWord,
        this.pokemonId,
        this.attempts,
        this.attemptsState,
        mutableListOf(),
        this.hasWon
    )
}
