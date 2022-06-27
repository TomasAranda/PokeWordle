package com.example.poke_wordle.data.network

import com.example.poke_wordle.domain.Pokemon
import com.google.gson.annotations.SerializedName

data class PokemonDto(
    val id: Int,
    val name: String,
    val sprites: PokemonSprites,
    val types: List<PokemonType>,
)

data class PokemonType (
    val slot: Int,
    val type: Type
)

data class Type (
    val name: String
)

data class PokemonSprites(
    val other: OtherSprites?
)

data class OtherSprites (
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtwork?
)

data class OfficialArtwork (
    @SerializedName("front_default")
    val frontDefault: String
)

internal fun PokemonDto.toDomainModel() =
    Pokemon(
        this.id,
        this.name,
        this.sprites.other?.officialArtwork?.frontDefault!!,
        this.types.map { it.type.name }
    )