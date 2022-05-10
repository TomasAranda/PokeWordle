package com.example.tp_mobile.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {
    @GET("/{id}")
    suspend fun getPokemon(@Path("id") pokemonId: Int) : Response<Pokemon>

    @GET
    suspend fun getPokemonList(@Query("limit") quantity: Int = 898) : Response<List<PokemonFromList>>
}