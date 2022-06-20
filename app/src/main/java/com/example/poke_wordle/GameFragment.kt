package com.example.poke_wordle

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.poke_wordle.databinding.FragmentGameBinding
import com.example.poke_wordle.db.AppDatabase
import com.example.poke_wordle.domain.PokeWordle
import com.example.poke_wordle.network.PokemonService
import com.example.poke_wordle.repository.PokeWordlePlayRepository
import com.example.poke_wordle.repository.PokemonRepository
import com.example.poke_wordle.viewmodel.PokeWordleViewModel

class GameFragment : Fragment() {
    private lateinit var binding: FragmentGameBinding
    private lateinit var wordleViewModel: PokeWordleViewModel
    private val args: GameFragmentArgs by navArgs()

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
        binding = FragmentGameBinding.inflate(inflater)

        wordleViewModel.wordle.observe(viewLifecycleOwner) { wordle ->
            if (wordle != null) {
                addWordleLetterViews(wordle)
            }
        }

        val levelsArray = resources.getStringArray(R.array.levels)
        binding.difficultyLevel.text = args.chosenGameLevel
        if (args.chosenGameLevel == levelsArray[2]) { // Difícil
            binding.hintButton.visibility = View.GONE
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
        view?.findViewById<Button>(R.id.hint_button)?.visibility = View.GONE
    }

    private fun addWordleLetterViews(wordlePlay: PokeWordle) {
        for ((index, _) in wordlePlay.solutionWord.withIndex()) {
            binding.apply {
                wordleRow1.addView(createTextView(), index)
                wordleRow2.addView(createTextView(), index)
                wordleRow3.addView(createTextView(), index)
                wordleRow4.addView(createTextView(), index)
                wordleRow5.addView(createTextView(), index)
                wordleRow6.addView(createTextView(), index)
            }
        }
    }

    private fun createTextView(): TextView {
        val tv = TextView(context)
        tv.gravity = Gravity.CENTER
        tv.textSize = 30F
        tv.setTextColor(resources.getColor(R.color.black))
        tv.setBackgroundColor(resources.getColor(R.color.white))
        tv.typeface = Typeface.DEFAULT_BOLD

        tv.setPadding(5, 5, 5, 5)
        val params = LinearLayout.LayoutParams(
            100, 120
        )
        params.setMargins(10,0, 10, 0)

        tv.layoutParams = params
        return tv
    }

}