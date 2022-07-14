package com.example.poke_wordle.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.poke_wordle.R
import com.example.poke_wordle.databinding.FragmentWelcomeBinding
import com.example.poke_wordle.ui.viewmodel.PokeWordleViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class WelcomeFragment : Fragment() {
    private val wordleViewModel by sharedViewModel<PokeWordleViewModel>()
    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentWelcomeBinding.inflate(inflater)

        wordleViewModel.wordle.observe(viewLifecycleOwner) { wordle ->
            if (wordle != null) {
                binding.playButton.setOnClickListener {
                    findNavController().navigate(R.id.action_welcomeFragment_to_gameFragment)
                }
            } else {
                binding.playButton.setOnClickListener {
                    showLevelPickerDialog {
                        findNavController().navigate(R.id.action_welcomeFragment_to_gameFragment)
                    }
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.statsButton.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_statsFragment)
        }

        binding.helpButton.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_helpFragment)
        }
    }

    private fun showLevelPickerDialog(navigationAction: () -> Unit) {
        val dialog = LevelsPickerDialogFragment(navigationAction)
        dialog.show(parentFragmentManager, "Select level")
    }

}