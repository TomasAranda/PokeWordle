package com.example.poke_wordle.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.poke_wordle.R
import com.example.poke_wordle.ui.viewmodel.PokeWordleViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class WelcomeFragment : Fragment() {
    private val wordleViewModel by sharedViewModel<PokeWordleViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playButton = view.findViewById<Button>(R.id.play_button)
        wordleViewModel.wordle.observe(viewLifecycleOwner) { wordle ->
            if (wordle != null) {
                playButton.setOnClickListener {
                    findNavController().navigate(R.id.action_welcomeFragment_to_gameFragment)
                }
            } else {
                playButton.setOnClickListener {
                    showLevelPickerDialog {
                        findNavController().navigate(R.id.action_welcomeFragment_to_gameFragment)
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

    private fun showLevelPickerDialog(navigationAction: () -> Unit) {
        val dialog = LevelsPickerDialogFragment(navigationAction)
        dialog.show(parentFragmentManager, "Select level")
    }

}