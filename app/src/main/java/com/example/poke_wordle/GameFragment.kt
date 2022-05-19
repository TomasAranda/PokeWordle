package com.example.poke_wordle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.poke_wordle.network.PokemonService
import com.example.poke_wordle.picasso.transformations.MaskTransformation
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        val pokemonIV = view.findViewById<ImageView>(R.id.pokemon_image_view)
        view.findViewById<TextView>(R.id.difficulty_level).text = args.chosenGameLevel
        view.findViewById<Button>(R.id.hint_button).setOnClickListener {
            getRandomPokemonImage(pokemonIV)
        }
    }

    private fun getRandomPokemonImage(pokemonIV: ImageView) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = PokemonService.create().getPokemon(args.randomPokemonId)
            val pokemonImageUrl = call.body()?.sprites?.other?.officialArtwork?.frontDefault
            if (call.isSuccessful) {
                if (pokemonImageUrl != null) {
                    activity?.runOnUiThread {
                        Picasso.get()
                            .load(pokemonImageUrl)
                            .transform(MaskTransformation(requireContext(), R.color.black))
                            .into(pokemonIV)
                    }
                }
            }
        }
    }
}