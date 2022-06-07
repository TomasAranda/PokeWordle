package com.example.poke_wordle

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.poke_wordle.db.AppDatabase
import com.example.poke_wordle.network.PokemonService
import com.example.poke_wordle.repository.PokeWordlePlayRepository
import com.example.poke_wordle.repository.PokemonRepository
import com.example.poke_wordle.viewmodel.LevelPickerViewModel

class LevelsPickerDialogFragment(val onSelect: (String) -> Unit) : DialogFragment() {
    private lateinit var levelPickerViewModel : LevelPickerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pokemonService = PokemonService.create()
        val pokemonDao = context?.let { AppDatabase.getInstance(it).pokemonDao() }
        val pokemonRepository = PokemonRepository(pokemonService, pokemonDao!!)

        val pokeWordlePlayDao = context?.let { AppDatabase.getInstance(it).pokeWordlePlayDao() }
        val pokeWordlePlayRepository = PokeWordlePlayRepository(pokeWordlePlayDao!!)

        // Lastly, create an instance of LoginViewModel with userRepository
        levelPickerViewModel = LevelPickerViewModel(pokemonRepository, pokeWordlePlayRepository)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.pick_level)
                .setItems(R.array.levels) { _, which ->
                    val level = resources.getStringArray(R.array.levels)[which]
                    levelPickerViewModel.createNewGame(level)
                    onSelect(level)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}