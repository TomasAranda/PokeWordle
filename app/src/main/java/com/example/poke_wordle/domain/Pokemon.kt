package com.example.poke_wordle.domain

import java.io.Serializable

data class Pokemon (
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>
) : Serializable