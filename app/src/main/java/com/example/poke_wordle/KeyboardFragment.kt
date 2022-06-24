package com.example.poke_wordle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.forEach
import com.example.poke_wordle.databinding.FragmentKeyboardBinding
import com.example.poke_wordle.db.AppDatabase
import com.example.poke_wordle.network.PokemonService
import com.example.poke_wordle.repository.PokeWordlePlayRepository
import com.example.poke_wordle.repository.PokemonRepository
import com.example.poke_wordle.viewmodel.PokeWordleViewModel

class KeyboardFragment() : Fragment() {
    private lateinit var binding: FragmentKeyboardBinding
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKeyboardBinding.inflate(inflater)

        binding.firstRow.forEach {
            it.setOnClickListener { button ->
                wordleViewModel.addLetter((button as Button).text.single())
            }
        }
        binding.secondRow.forEach {
            it.setOnClickListener { button ->
                wordleViewModel.addLetter((button as Button).text.single())
            }
        }
        binding.thirdRow.forEach {
            it.setOnClickListener { button ->
                if ((button as Button).text.length == 1)
                wordleViewModel.addLetter(button.text.single())
            }
        }

        binding.backspace.setOnClickListener {
            wordleViewModel.removeLastLetter()
        }
        binding.enter.setOnClickListener {
            wordleViewModel.addGuess()
        }


        return binding.root
    }

}