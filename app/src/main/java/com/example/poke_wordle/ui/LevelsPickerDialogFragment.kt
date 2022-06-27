package com.example.poke_wordle.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.poke_wordle.R
import com.example.poke_wordle.data.db.AppDatabase
import com.example.poke_wordle.data.network.PokemonService
import com.example.poke_wordle.data.repository.PokeWordlePlayRepository
import com.example.poke_wordle.data.repository.PokemonRepository
import com.example.poke_wordle.ui.viewmodel.PokeWordleViewModel

class LevelsPickerDialogFragment(val onSelect: (String) -> Unit) : DialogFragment() {
    private lateinit var wordleViewModel: PokeWordleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getInstance(requireContext())
        val pokemonDao = db.pokemonDao()
        val pokeWordlePlayDao = db.pokeWordlePlayDao()
        val service = PokemonService.create()
        val pokemonRepository = PokemonRepository(service, pokemonDao)
        val pokeWordlePlayRepository = PokeWordlePlayRepository(pokeWordlePlayDao)
        wordleViewModel = PokeWordleViewModel(pokemonRepository, pokeWordlePlayRepository)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.d("LEVEL PICKER", "WORDLE: ${wordleViewModel.wordle.value.toString()}")
        Log.d("LEVEL PICKER", "POKEMON OF THE DAY: ${wordleViewModel.pokemonOfTheDay.value.toString()}")
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.pick_level)
                .setItems(R.array.levels) { _, which ->
                    val level = resources.getStringArray(R.array.levels)[which]
                    wordleViewModel.createNewGame(level)
                    onSelect(level)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}