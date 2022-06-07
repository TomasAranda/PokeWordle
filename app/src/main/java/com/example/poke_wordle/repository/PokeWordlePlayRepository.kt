package com.example.poke_wordle.repository

import com.example.poke_wordle.db.PokeWordlePlayDao
import com.example.poke_wordle.db.model.PokeWordlePlayEntity
import java.util.Calendar

class PokeWordlePlayRepository(
    private val pokeWordlePlayDao: PokeWordlePlayDao
) {
    suspend fun newGame(level: String, pokemonId: Int) {
        val newGame = PokeWordlePlayEntity(Calendar.getInstance(), pokemonId, 0, level)
        pokeWordlePlayDao.insert(newGame)
    }

}