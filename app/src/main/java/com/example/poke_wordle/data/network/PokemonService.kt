package com.example.poke_wordle.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonService {
    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id") pokemonId: Int) : PokemonDto

    companion object {
        private const val BASE_URL = "https://pokeapi.co/api/v2/"

        fun create(): PokemonService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PokemonService::class.java)
        }
    }
}