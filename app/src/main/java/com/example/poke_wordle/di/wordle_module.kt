package com.example.poke_wordle.di

import com.example.poke_wordle.data.db.AppDatabase
import com.example.poke_wordle.data.db.model.WordlePlayEntityMapper
import com.example.poke_wordle.data.network.PokemonService
import com.example.poke_wordle.data.repository.PokeWordlePlayRepository
import com.example.poke_wordle.data.repository.PokemonRepository
import com.example.poke_wordle.ui.viewmodel.PokeWordleViewModel
import com.example.poke_wordle.ui.viewmodel.StatsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val wordleModule = module {
    single { AppDatabase.getInstance(get()).pokeWordlePlayDao() }
    single { AppDatabase.getInstance(get()).pokemonDao() }
    single { PokemonService.create() }
    single { WordlePlayEntityMapper() }
    single { PokemonRepository(get(), get()) }
    single { PokeWordlePlayRepository(get(), get()) }

    viewModel { PokeWordleViewModel(get(), get()) }
    viewModel { StatsViewModel(get()) }
}