package com.example.poke_wordle.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.core.view.forEachIndexed
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.poke_wordle.BuildConfig
import com.example.poke_wordle.R
import com.example.poke_wordle.databinding.FragmentGameBinding
import com.example.poke_wordle.domain.LetterState
import com.example.poke_wordle.domain.PokeWordle
import com.example.poke_wordle.ui.viewmodel.PokeWordleViewModel
import com.example.poke_wordle.ui.views.WordleLetterView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class GameFragment : Fragment() {
    private lateinit var binding: FragmentGameBinding
    private val wordleViewModel by sharedViewModel<PokeWordleViewModel>()
    private var currentRowResource = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentRowResource = resources.getIdentifier("wordle_row_1", "id",
            BuildConfig.APPLICATION_ID
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater)

        wordleViewModel.wordle.observe(viewLifecycleOwner) { wordle ->
            if (wordle != null) {
                updateCurrentRow(wordle.attempts + 1)
                addWordleLetterViews(wordle)
                updateLettersState(wordle)
                if (wordle.hasWon || wordle.attempts == 6) {
                    showGameOverDialog(wordle.hasWon)
                }
            }
        }

        wordleViewModel.currentGuess.observe(viewLifecycleOwner) {
            updateCurrentGuess(it)
        }

        wordleViewModel.wordle.observe(viewLifecycleOwner) { wordle ->
            val levelsArray = resources.getStringArray(R.array.levels)
            binding.difficultyLevel.text = wordle?.level
            if (wordle?.level == levelsArray[2]) { // Difícil
                removeHintButton()
            } else {
                binding.hintButton.setOnClickListener {
                    when (wordle?.level) {
                        levelsArray[0] -> showHintImageDialog(false)// Fácil
                        levelsArray[1] -> showHintImageDialog(true) // Intermedio
                    }
                }
            }
        }

        return binding.root
    }

    private fun showGameOverDialog(hasWon: Boolean) {
        val dialog = GameOverDialogFragment(hasWon)
        dialog.show(parentFragmentManager, "Game Over Dialog")
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
                        wordleRow1.addView(WordleLetterView(requireContext()), index)
                        wordleRow2.addView(WordleLetterView(requireContext()), index)
                        wordleRow3.addView(WordleLetterView(requireContext()), index)
                        wordleRow4.addView(WordleLetterView(requireContext()), index)
                        wordleRow5.addView(WordleLetterView(requireContext()), index)
                        wordleRow6.addView(WordleLetterView(requireContext()), index)
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
                    val letterView = rowLinearlayout?.get(letterIndex) as WordleLetterView
                    letterView.isChecked = true
                    when(letterState) {
                        LetterState.CORRECT -> { letterView.isCorrect = true }
                        LetterState.WRONG_POSITION -> { letterView.isPresent = true }
                        else -> {}
                    }
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