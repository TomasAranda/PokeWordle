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
import java.time.LocalDate

class WelcomeFragment : Fragment() {
    private var pokemonOfTheDayId: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<CustomButtonView>(R.id.wordle_letter_button).setOnClickListener {
            (it as CustomButtonView).letterState = LetterState.CORRECT
        }

        view.findViewById<Button>(R.id.play_button).setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                // TODO("Move this logic to ViewModel [CHECK IF THERE IS CURRENT WORDLEPLAY ON DB]")
                if (AppDatabase.getInstance(requireContext()).pokeWordlePlayDao().getWordlePlay(
                        LocalDate.now()) != null) {
                    activity?.runOnUiThread {
                        val action = WelcomeFragmentDirections.actionWelcomeFragmentToGameFragment("Facil", pokemonOfTheDayId)
                        findNavController().navigate(action)
                    }
                } else {
                    activity?.runOnUiThread {
                        showLevelPickerDialog {
                            val action = WelcomeFragmentDirections.actionWelcomeFragmentToGameFragment(it, pokemonOfTheDayId)
                            findNavController().navigate(action)
                        }
                    }
                }
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
        val dialog  = LevelsPickerDialogFragment(navigationAction)
        dialog.show(parentFragmentManager, "Select level")
    }

}