package com.example.poke_wordle

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.forEachIndexed
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.poke_wordle.databinding.FragmentGameBinding
import com.example.poke_wordle.db.AppDatabase
import com.example.poke_wordle.domain.LetterState
import com.example.poke_wordle.domain.PokeWordle
import com.example.poke_wordle.network.PokemonService
import com.example.poke_wordle.repository.PokeWordlePlayRepository
import com.example.poke_wordle.repository.PokemonRepository
import com.example.poke_wordle.viewmodel.PokeWordleViewModel

class GameFragment : Fragment() {
    private lateinit var binding: FragmentGameBinding
    private lateinit var wordleViewModel: PokeWordleViewModel
    private var currentRowResource = 0
    private val args: GameFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentRowResource = resources.getIdentifier("wordle_row_1", "id", BuildConfig.APPLICATION_ID)

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
        binding = FragmentGameBinding.inflate(inflater)

        wordleViewModel.wordle.observe(viewLifecycleOwner) { wordle ->
            if (wordle != null) {
                addWordleLetterViews(wordle)
            }
            wordle?.let {
                wordleViewModel.currentGuess.value?.let { guess -> updateLettersState(guess, it.solutionWord) }
                updateCurrentRow(it.attempts + 1)
            }
        }

        binding.addLetter.setOnClickListener {
            wordleViewModel.addLetter('p')
        }
        binding.removeLetter.setOnClickListener {
            wordleViewModel.removeLastLetter()
        }
        binding.enter.setOnClickListener {
            wordleViewModel.addGuess()
        }
        wordleViewModel.currentGuess.observe(viewLifecycleOwner) {
            updateCurrentGuess(it)
        }

        val levelsArray = resources.getStringArray(R.array.levels)
        binding.difficultyLevel.text = args.chosenGameLevel
        if (args.chosenGameLevel == levelsArray[2]) { // Difícil
            removeHintButton()
        } else {
            binding.hintButton.setOnClickListener {
                when (args.chosenGameLevel) {
                    levelsArray[0] -> showHintImageDialog(false)// Fácil
                    levelsArray[1] -> showHintImageDialog(true) // Intermedio
                }
            }
        }

        return binding.root
    }

    private fun showHintImageDialog(showsPokemonType: Boolean) {
        val dialog = PokemonImageHintDialog(showsPokemonType)
        dialog.show(parentFragmentManager, "Pokemon Image Hint")
        removeHintButton()
    }

    private fun removeHintButton() {
        binding.hintButton.visibility = View.GONE
        val spacer = Space(context)
        spacer.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 180
        )
        binding.root.addView(spacer, 7)
    }

    private fun addWordleLetterViews(wordlePlay: PokeWordle) {
        for ((index, letter) in wordlePlay.solutionWord.withIndex()) {
            if (letter != '-') {
                binding.apply {
                    wordleRow1.addView(createWordleLetterView(), index)
                    wordleRow2.addView(createWordleLetterView(), index)
                    wordleRow3.addView(createWordleLetterView(), index)
                    wordleRow4.addView(createWordleLetterView(), index)
                    wordleRow5.addView(createWordleLetterView(), index)
                    wordleRow6.addView(createWordleLetterView(), index)
                }
            } else {
                binding.apply {
                    wordleRow1.addView(createWordleSpacerView(), index)
                    wordleRow2.addView(createWordleSpacerView(), index)
                    wordleRow3.addView(createWordleSpacerView(), index)
                    wordleRow4.addView(createWordleSpacerView(), index)
                    wordleRow5.addView(createWordleSpacerView(), index)
                    wordleRow6.addView(createWordleSpacerView(), index)
                }
            }
        }
    }

    private fun createWordleLetterView(): TextView {
        val tv = TextView(context)
        tv.gravity = Gravity.CENTER
        tv.textSize = 30F
        tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        tv.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        tv.typeface = Typeface.DEFAULT_BOLD

        tv.setPadding(5, 5, 5, 5)
        val params = LinearLayout.LayoutParams(
            100, 120
        )
        params.setMargins(10,0, 10, 0)

        tv.layoutParams = params
        return tv
    }

    private fun createWordleSpacerView(): Space {
        val spacer = Space(context)
        spacer.layoutParams = LinearLayout.LayoutParams(
            30, 120
        )
        return spacer
    }

    private fun updateCurrentRow(current: Int) {
        currentRowResource = resources.getIdentifier("wordle_row_${current}", "id", BuildConfig.APPLICATION_ID)
    }

    private fun updateLettersState(guess: String, solutionWord: String) {
        val guessState = MutableList(solutionWord.length) { LetterState.INCORRECT }
        guess.forEachIndexed { index, letter ->
            if (solutionWord.contains(letter, true)) {
                if (solutionWord[index] == guess[index]) {
                    guessState[index] = LetterState.CORRECT
                } else {
                    guessState[index] = LetterState.WRONG_POSITION
                }
            }
        }

    }

    private fun updateCurrentGuess(guess: String) {
        val rowLinearlayout = view?.findViewById<LinearLayout>(currentRowResource)
        rowLinearlayout?.forEachIndexed { index, view ->
            if (index < guess.length) {
                (view as TextView).text = guess[index].toString().uppercase()
            } else {
                (view as TextView).text = ""
            }
        }
    }

}