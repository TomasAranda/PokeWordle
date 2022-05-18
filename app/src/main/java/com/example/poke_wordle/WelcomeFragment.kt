package com.example.poke_wordle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.poke_wordle.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WelcomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.play_button).setOnClickListener {
            showLevelPickerDialog {
                val pokemonId = getRandomPokemonId()
                val action = WelcomeFragmentDirections.actionWelcomeFragmentToGameFragment(it, pokemonId)
                findNavController().navigate(action)
            }
        }

        view.findViewById<Button>(R.id.stats_button).setOnClickListener {
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToStatsFragment()
            findNavController().navigate(action)
        }


        view.findViewById<Button>(R.id.help_button).setOnClickListener {
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToHelpFragment()
            findNavController().navigate(action)
        }
    }

    private fun showLevelPickerDialog(navigationAction: (String) -> Unit) {
        val dialog  = LevelsDialogFragment(navigationAction)
        dialog.show(parentFragmentManager, "Select level")
    }

    private fun getRandomPokemonId() : Int {
        var randomPokemonId = 0
        CoroutineScope(Dispatchers.IO).launch {
            val db = context?.let { AppDatabase.getInstance(it) }
            val pokemon = db?.pokemonDao()?.getRandomPokemonFromList()
            if (pokemon != null) {
                randomPokemonId = pokemon.id
            }
        }
        return randomPokemonId
    }

}