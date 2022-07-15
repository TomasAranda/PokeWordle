package com.example.poke_wordle.data.network

import com.example.poke_wordle.domain.Pokemon

class PokemonDtoMapper {
    fun mapToDomainModel(model: PokemonDto): Pokemon {
        return Pokemon(
            id = model.id,
            name = model.name.uppercase(),
            imageUrl = model.sprites.other?.officialArtwork?.frontDefault!!,
            types = List(2) {""}
        )
    }
}