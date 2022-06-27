package com.example.poke_wordle

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.forEach
import androidx.core.view.get
import com.example.poke_wordle.databinding.FragmentKeyboardBinding
import com.example.poke_wordle.db.AppDatabase
import com.example.poke_wordle.domain.LetterState
import com.example.poke_wordle.domain.PokeWordle
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

        wordleViewModel.wordle.observe(viewLifecycleOwner) { wordle ->
            if (wordle != null) {
                updateLettersState(wordle)
            }
        }

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

    private fun updateLettersState(wordle: PokeWordle) {
        val usedLetters = mutableListOf<Pair<Char, LetterState>>()
        for (guess in wordle.attemptsState) {
            if (guess != "") {
                guess.forEachIndexed { letterIndex, letter ->
                    val letterState = getLetterState(letter, letterIndex, wordle.solutionWord)
                    val duplicateLetter = usedLetters.find { it.first == letter }
                    val duplicateLetterIndex = usedLetters.indexOf(duplicateLetter)

                    if (duplicateLetterIndex > 0) {
                        when(duplicateLetter?.second) {
                            LetterState.WRONG_POSITION -> {
                                if (letterState == LetterState.CORRECT) usedLetters[duplicateLetterIndex] = letter to letterState
                            }
                            else -> { }
                        }
                    } else {
                        usedLetters.add(letter to letterState)
                    }
                }
            }
        }
        binding.firstRow.forEach { view ->
            val letterPair = usedLetters.find { it.first == (view as Button).text.single() }
            letterPair?.let {
                view.setBackgroundTintList(ColorStateList.valueOf(getLetterStateColor(it.second)))
            }
        }
        binding.secondRow.forEach { view ->
            val letterPair = usedLetters.find { it.first == (view as Button).text.single() }
            letterPair?.let {
                view.setBackgroundTintList(ColorStateList.valueOf(getLetterStateColor(it.second)))
            }
        }
        binding.thirdRow.forEach { view ->
            if ((view as Button).text.length == 1) {
                val letterPair = usedLetters.find { it.first == view.text.single() }
                letterPair?.let {
                    view.setBackgroundTintList(ColorStateList.valueOf(getLetterStateColor(it.second)))
                }
            }
        }
    }

    private fun getLetterStateColor(letterState: LetterState): Int {
        return when(letterState) {
            LetterState.CORRECT -> { Color.parseColor("#6aaa64") }
            LetterState.WRONG_POSITION -> { Color.parseColor("#c9b458") }
            LetterState.INCORRECT -> { Color.parseColor("#787c7e") }
        }
    }

    private fun getLetterState(letter: Char, index: Int, solutionWord: String): LetterState {
        if (solutionWord.contains(letter)) {
            return if (solutionWord[index] == letter) {
                LetterState.CORRECT
            } else {
                LetterState.WRONG_POSITION
            }
        }
        return LetterState.INCORRECT
    }
}