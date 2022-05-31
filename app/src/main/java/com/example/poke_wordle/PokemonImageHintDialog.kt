package com.example.poke_wordle

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.poke_wordle.network.PokemonService
import com.example.poke_wordle.util.MaskTransformation
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class PokemonImageHintDialog(private val showsPokemonType: Boolean, private val pokemonId: Int) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // TODO("Agregar ViewBinding")
            // val binding = FragmentHintDialogBinding.inflate(layoutInflater)
            val dialogView = layoutInflater.inflate(R.layout.fragment_hint_dialog, null)
            if (showsPokemonType) {
                builder.setTitle("El Pokemon es de este tipo:")
                loadPokemonTypeImage(dialogView)
            } else {
                builder.setTitle("El Pokemon tiene esta forma")
                loadPokemonImage(dialogView)
            }
            builder.setView(dialogView)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun loadPokemonImage(dialogView: View) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = PokemonService.create().getPokemon(pokemonId)
            val pokemonImageUrl = call.body()?.sprites?.other?.officialArtwork?.frontDefault
            if (pokemonImageUrl != null) {
                val primaryView = dialogView.findViewById<RelativeLayout>(R.id.primary_image)
                val primaryIV = dialogView.findViewById<ImageView>(R.id.primary_image_iv)
                val primaryProgressBar = dialogView.findViewById<ProgressBar>(R.id.primary_image_pb)
                activity?.runOnUiThread {
                    val params = primaryView.layoutParams
                    params.height = 1000
                    params.width = 1000
                    primaryView.requestLayout()
                    Picasso.get()
                        .load(pokemonImageUrl)
                        .transform(MaskTransformation(requireContext(), R.color.black))
                        .into(primaryIV, object : Callback {
                            override fun onSuccess() {
                                primaryProgressBar.visibility = View.GONE
                            }

                            override fun onError(e: Exception?) {
                                TODO("Not yet implemented")
                            }

                        })
                }
            }
        }
    }

    private fun loadPokemonTypeImage(dialogView: View) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = PokemonService.create().getPokemon(pokemonId)
            val types = call.body()?.types
            if (types != null) {
                val primaryIV = dialogView.findViewById<ImageView>(R.id.primary_image_iv)
                val primaryProgressBar = dialogView.findViewById<ProgressBar>(R.id.primary_image_pb)
                if (types.size > 1) {
                    val secondaryView = dialogView.findViewById<RelativeLayout>(R.id.secondary_image)
                    val secondaryIV = dialogView.findViewById<ImageView>(R.id.secondary_image_iv)
                    val secondaryProgressBar = dialogView.findViewById<ProgressBar>(R.id.secondary_image_pb)

                    activity?.runOnUiThread {
                        secondaryView.visibility = View.VISIBLE
                        Picasso.get()
                            .load(getPokemonTypeIconResource(types[1].type.name))
                            .into(secondaryIV, object: Callback {
                                override fun onSuccess() {
                                    secondaryProgressBar.visibility = View.GONE
                                }

                                override fun onError(e: Exception?) {
                                    TODO("Not yet implemented")
                                }
                            })
                    }
                }
                activity?.runOnUiThread {
                    Picasso.get()
                        .load(getPokemonTypeIconResource(types[0].type.name))
                        .into(primaryIV, object: Callback {
                            override fun onSuccess() {
                                primaryProgressBar.visibility = View.GONE
                            }

                            override fun onError(e: Exception?) {
                                TODO("Not yet implemented")
                            }
                        })
                }
            }
        }
    }

    private fun getPokemonTypeIconResource(type: String) : Int {
        return resources.getIdentifier("${type}_type_icon", "drawable", BuildConfig.APPLICATION_ID)
    }

}