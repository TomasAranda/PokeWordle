package com.example.poke_wordle.repository

import com.example.poke_wordle.db.PokemonDao
import com.example.poke_wordle.db.model.toDomainModel
import com.example.poke_wordle.domain.Pokemon
import com.example.poke_wordle.network.PokemonService
import com.example.poke_wordle.network.toDomainModel

class PokemonRepository(
    private val pokemonService: PokemonService,
    private val pokemonDao: PokemonDao
) {

    suspend fun get(id: Int): Pokemon {
        return pokemonService.getPokemon(id).toDomainModel()
    }

    suspend fun getRandomFromDB(): Pokemon {
        return pokemonDao.getRandomPokemonFromList().toDomainModel()
    }

}