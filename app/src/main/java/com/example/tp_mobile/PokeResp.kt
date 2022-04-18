package com.example.tp_mobile

import com.google.gson.annotations.SerializedName

data class PokeResp(
    //@SerializedName("url") var url: String,
    //@SerializedName("name") var pokemon: List<String>
    @SerializedName("status") var url: String,
    @SerializedName("message") var pokemon: List<String>
)
