package com.example.poke_wordle

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.poke_wordle.databinding.FragmentHintDialogBinding
import com.example.poke_wordle.db.AppDatabase
import com.example.poke_wordle.network.PokemonService
import com.example.poke_wordle.repository.PokeWordlePlayRepository
import com.example.poke_wordle.repository.PokemonRepository
import com.example.poke_wordle.util.MaskTransformation
import com.example.poke_wordle.viewmodel.PokeWordleViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class PokemonImageHintDialog(private val showsPokemonType: Boolean) : DialogFragment() {
    private lateinit var wordleViewModel: PokeWordleViewModel
    private lateinit var binding: FragmentHintDialogBinding

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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHintDialogBinding.inflate(layoutInflater)
        wordleViewModel.wordle.observe(viewLifecycleOwner) {
            binding.dialogTitle.text = if (it?.level == "Fácil") "El Pokemon tiene esta forma" else "El Pokemon es de este tipo"
        }
        wordleViewModel.pokemonOfTheDay.observe(viewLifecycleOwner) { pokemon ->
            if (showsPokemonType) {
                pokemon?.let { loadPokemonTypeImage(binding, it.types) }
            } else {
                pokemon?.let { loadPokemonImage(binding, it.imageUrl) }
            }
        }
        return binding.root
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
                    e?.message?.let { Log.e("IMAGE HINT LOADING ERROR", it) }
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