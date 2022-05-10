package com.example.tp_mobile.network

import com.google.gson.annotations.SerializedName

data class Pokemon(
    val id: Int,
    val name: String,
    val sprites: PokemonSprites
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
    val frontDefault: String?
)

data class PokemonList(
    val count: Int,
    val results: List<PokemonFromList>
)

data class PokemonFromList(
    val name: String,
    val url: String
)