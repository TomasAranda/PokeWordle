package com.example.poke_wordle

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.poke_wordle.network.PokemonService
import com.example.poke_wordle.picasso.transformations.MaskTransformation
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PokemonImageHintDialog(val showsPokemonType: Boolean, val pokemonId: Int) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            if (showsPokemonType) {
                builder.setTitle("El Pokemon es de este tipo:")
                val dialogView = layoutInflater.inflate(R.layout.fragment_hint_dialog, null)
                val primaryTypeImageView = dialogView.findViewById<ImageView>(R.id.primary_type)
                val secondaryTypeImageView = dialogView.findViewById<ImageView>(R.id.secondary_type)
                getPokemonTypeImage(primaryTypeImageView, secondaryTypeImageView)
                builder.setView(dialogView)
            } else {
                builder.setTitle("El Pokemon tiene esta forma")
                val imageView = ImageView(requireContext())
                getPokemonImage(imageView)
                builder.setView(imageView)
            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun getPokemonImage(pokemonIV: ImageView) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = PokemonService.create().getPokemon(pokemonId)
            val pokemonImageUrl = call.body()?.sprites?.other?.officialArtwork?.frontDefault
            if (call.isSuccessful) {
                if (pokemonImageUrl != null) {
                    activity?.runOnUiThread {
                        Picasso.get()
                            .load(pokemonImageUrl)
                            .resize(1000, 1000)
                            .transform(MaskTransformation(requireContext(), R.color.black))
                            .into(pokemonIV)
                    }
                }
            }
        }
    }

    private fun getPokemonTypeImage(pokemonPrimaryTypeIV: ImageView, pokemonSecondaryTypeIV: ImageView) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = PokemonService.create().getPokemon(pokemonId)
            val types = call.body()?.types
            if (types != null) {
                activity?.runOnUiThread {
                    Picasso.get()
                        .load(getPokemonTypeIcon(types[0].type.name))
                        .into(pokemonPrimaryTypeIV)
                }
                if (types.size > 1) {
                    activity?.runOnUiThread {
                        pokemonSecondaryTypeIV.visibility = View.VISIBLE
                        Picasso.get()
                            .load(getPokemonTypeIcon(types[1].type.name))
                            .into(pokemonSecondaryTypeIV)
                    }
                }
            }
        }
    }

    private fun getPokemonTypeIcon(type: String) : Int {
        return resources.getIdentifier("${type}_type_icon", "drawable", BuildConfig.APPLICATION_ID)
    }

}