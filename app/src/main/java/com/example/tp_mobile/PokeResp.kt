package com.example.tp_mobile

import com.google.gson.annotations.SerializedName

data class PokeResp(
    @SerializedName("id") var url: String,
    @SerializedName("sprites") var pokemon: List<String>
    //@SerializedName("status") var url: String,
    //@SerializedName("message") var pokemon: List<String>

)
