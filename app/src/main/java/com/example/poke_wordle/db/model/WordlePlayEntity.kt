package com.example.poke_wordle.db.model

import android.util.Log
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
    var attemptsState: MutableList<String> = MutableList(6) { "" },
    var hasWon: Boolean,
    val level: String
)

internal fun WordlePlayEntity.toDomainModel(): PokeWordle {
    Log.d("DOMAIN MAPPER", this.attemptsState.toString())
    val attemptsList = List(6) {
        val guess = this.attemptsState[it]
        if (guess != "") guess.split("").single().toMutableList() else guess.toMutableList()
    }

    return PokeWordle(
        this.solutionWord,
        this.pokemonId,
        attemptsList
    )
}
