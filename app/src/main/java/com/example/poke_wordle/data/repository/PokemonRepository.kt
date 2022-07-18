package com.example.poke_wordle.data.repository

import com.example.poke_wordle.data.db.PokemonDao
import com.example.poke_wordle.data.network.PokemonDtoMapper
import com.example.poke_wordle.domain.Pokemon
import com.example.poke_wordle.data.network.PokemonService

class PokemonRepository(
    private val pokemonService: PokemonService,
    private val pokemonDao: PokemonDao,
    private val mapper: PokemonDtoMapper
) {

    suspend fun get(id: Int): Pokemon {
        return mapper.mapToDomainModel(pokemonService.getPokemon(id))
    }

    suspend fun getRandomFromDB(): Pokemon {
        val randomId = pokemonDao.selectRandomPokemon().id
        return mapper.mapToDomainModel(pokemonService.getPokemon(randomId))
    }

}