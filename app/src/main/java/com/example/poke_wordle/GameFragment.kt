package com.example.poke_wordle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs

class GameFragment : Fragment() {
    private val args: GameFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val levelsArray = resources.getStringArray(R.array.levels)
        view.findViewById<TextView>(R.id.difficulty_level).text = args.chosenGameLevel
        val hintButtonView = view.findViewById<Button>(R.id.hint_button)
        if (args.chosenGameLevel == levelsArray[2]) { // Difícil
            hintButtonView.visibility = View.GONE
        } else {
            hintButtonView.setOnClickListener {
                when (args.chosenGameLevel) {
                    levelsArray[0] -> showHintImageDialog(false, args.randomPokemonId)// Fácil
                    levelsArray[1] -> showHintImageDialog(true, args.randomPokemonId) // Intermedio
                }
            }
        }

    }

    private fun showHintImageDialog(showsPokemonType: Boolean, pokemonId: Int) {
        val dialog = PokemonImageHintDialog(showsPokemonType, pokemonId)
        dialog.show(parentFragmentManager, "Pokemon Image Hint")
        view?.findViewById<Button>(R.id.hint_button)?.visibility = View.GONE
    }

    private fun showImageWinLoose(ganador:Boolean){
        val ganador = true;
    }

}