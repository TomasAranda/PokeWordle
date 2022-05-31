package com.example.poke_wordle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.poke_wordle.databinding.FragmentGameBinding
import com.example.poke_wordle.viewmodel.PokeWordleViewModel

class GameFragment : Fragment() {
    private lateinit var binding: FragmentGameBinding
    private val wordleViewModel: PokeWordleViewModel by viewModels()
    private val args: GameFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater)

        wordleViewModel.new()
        wordleViewModel.wordle.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.attempts.text = it.attempts.toString()
            }
        }

        binding.attempts.setOnClickListener {
            wordleViewModel.addLetter('p')
        }
        binding.difficultyLevel.setOnClickListener {
            wordleViewModel.removeLastLetter()
        }

        val levelsArray = resources.getStringArray(R.array.levels)
        binding.difficultyLevel.text = args.chosenGameLevel
        if (args.chosenGameLevel == levelsArray[2]) { // Difícil
            binding.hintButton.visibility = View.GONE
        } else {
            binding.hintButton.setOnClickListener {
                when (args.chosenGameLevel) {
                    levelsArray[0] -> showHintImageDialog(false, args.randomPokemonId)// Fácil
                    levelsArray[1] -> showHintImageDialog(true, args.randomPokemonId) // Intermedio
                }
            }
        }

        return binding.root
    }

    private fun showHintImageDialog(showsPokemonType: Boolean, pokemonId: Int) {
        val dialog = PokemonImageHintDialog(showsPokemonType, pokemonId)
        dialog.show(parentFragmentManager, "Pokemon Image Hint")
        view?.findViewById<Button>(R.id.hint_button)?.visibility = View.GONE
    }

}