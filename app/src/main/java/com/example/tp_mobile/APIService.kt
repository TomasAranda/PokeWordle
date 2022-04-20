package com.example.tp_mobile

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {
    @GET
    suspend fun getPokemon(@Url url:String):Response<Pokemon>
}