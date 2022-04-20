package com.example.tp_mobile

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