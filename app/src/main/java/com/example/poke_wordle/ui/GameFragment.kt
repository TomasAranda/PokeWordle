package com.example.poke_wordle.ui

import android.graphics.Color
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
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.poke_wordle.BuildConfig
import com.example.poke_wordle.R
import com.example.poke_wordle.databinding.FragmentGameBinding
import com.example.poke_wordle.data.db.AppDatabase
import com.example.poke_wordle.domain.LetterState
import com.example.poke_wordle.domain.PokeWordle
import com.example.poke_wordle.data.network.PokemonService
import com.example.poke_wordle.data.repository.PokeWordlePlayRepository
import com.example.poke_wordle.data.repository.PokemonRepository
import com.example.poke_wordle.ui.viewmodel.PokeWordleViewModel

class GameFragment : Fragment() {
    private lateinit var binding: FragmentGameBinding
    private lateinit var wordleViewModel: PokeWordleViewModel
    private var currentRowResource = 0
    private val args: GameFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentRowResource = resources.getIdentifier("wordle_row_1", "id",
            BuildConfig.APPLICATION_ID
        )

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
                updateLettersState(wordle)
            }
        }

        wordleViewModel.currentGuessNumber.observe(viewLifecycleOwner) {
            updateCurrentRow(it)
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
        if (binding.wordleRow1.childCount != wordlePlay.solutionWord.length) {
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
        addAttempts(wordlePlay)
    }

    private fun addAttempts(wordlePlay: PokeWordle) {
        for ((index, attempt) in wordlePlay.attemptsState.withIndex()) {
            if (attempt.isNotEmpty()) {
                val rowResource = resources.getIdentifier("wordle_row_${index + 1}", "id",
                    BuildConfig.APPLICATION_ID
                )
                val rowLinearLayout = view?.findViewById<LinearLayout>(rowResource)
                rowLinearLayout?.forEachIndexed { viewIndex, view ->
                    (view as TextView).text = attempt[viewIndex].toString().uppercase()
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
        currentRowResource = resources.getIdentifier("wordle_row_${current}", "id",
            BuildConfig.APPLICATION_ID
        )
    }

    private fun updateLettersState(wordle: PokeWordle) {
        for ((rowIndex, guess) in wordle.attemptsState.withIndex()) {
            if (guess != "") {
                val rowResource = resources.getIdentifier("wordle_row_${rowIndex + 1}", "id",
                    BuildConfig.APPLICATION_ID
                )
                val rowLinearlayout = view?.findViewById<LinearLayout>(rowResource)
                guess.forEachIndexed { letterIndex, letter ->
                    val letterState = getLetterState(letter, letterIndex, wordle.solutionWord)
                    val letterViewColor = when(letterState) {
                        LetterState.CORRECT -> { Color.parseColor("#6aaa64") }
                        LetterState.WRONG_POSITION -> { Color.parseColor("#c9b458") }
                        LetterState.INCORRECT -> { Color.parseColor("#787c7e") }
                    }
                    rowLinearlayout?.get(letterIndex)?.setBackgroundColor(letterViewColor)
                }
            }
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