package com.example.poke_wordle

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.poke_wordle.databinding.FragmentHintDialogBinding
import com.example.poke_wordle.db.AppDatabase
import com.example.poke_wordle.domain.Pokemon
import com.example.poke_wordle.network.PokemonService
import com.example.poke_wordle.repository.PokemonRepository
import com.example.poke_wordle.util.MaskTransformation
import com.example.poke_wordle.viewmodel.HintImageViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class PokemonImageHintDialog(private val showsPokemonType: Boolean, private val pokemon: Pokemon) : DialogFragment() {
    private lateinit var viewModel: HintImageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = HintImageViewModel(
            pokemon,
            PokemonRepository(
                PokemonService.create(),
                AppDatabase.getInstance(requireContext()).pokemonDao()
            )
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val binding = FragmentHintDialogBinding.inflate(layoutInflater)
            if (showsPokemonType) {
                val pokemonType = viewModel.getPokemonType()
                builder.setTitle("El Pokemon es de este tipo:")
                loadPokemonTypeImage(binding, pokemonType)
            } else {
                val pokemonImageUrl = viewModel.getPokemonImageUrl()
                builder.setTitle("El Pokemon tiene esta forma")
                loadPokemonImage(binding, pokemonImageUrl)
            }
            builder.setView(binding.root)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun loadPokemonImage(binding: FragmentHintDialogBinding, pokemonImageUrl: String) {
        val primaryView = binding.primaryImage
        val primaryIV = binding.primaryImageIv
        val primaryProgressBar = binding.primaryImagePb

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
                    Toast.makeText(requireContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun loadPokemonTypeImage(binding: FragmentHintDialogBinding, pokemonType: List<String>) {
        val primaryIV = binding.primaryImageIv
        val primaryProgressBar = binding.primaryImagePb
        if (pokemonType.size > 1) {
            val secondaryView = binding.secondaryImage
            val secondaryIV = binding.secondaryImageIv
            val secondaryProgressBar = binding.secondaryImagePb

            secondaryView.visibility = View.VISIBLE
            loadImageResourceIntoImageView(secondaryIV, secondaryProgressBar, getPokemonTypeIconResource(pokemonType[1]))
        }
        loadImageResourceIntoImageView(primaryIV, primaryProgressBar, getPokemonTypeIconResource(pokemonType[0]))
    }

    private fun loadImageResourceIntoImageView(imageView: ImageView, progressBar: ProgressBar, resource: Int) {
        Picasso.get()
            .load(resource)
            .into(imageView, object: Callback {
                override fun onSuccess() {
                    progressBar.visibility = View.GONE
                }
                override fun onError(e: Exception?) {
                    Toast.makeText(requireContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun getPokemonTypeIconResource(type: String) : Int {
        return resources.getIdentifier("${type}_type_icon", "drawable", BuildConfig.APPLICATION_ID)
    }

}