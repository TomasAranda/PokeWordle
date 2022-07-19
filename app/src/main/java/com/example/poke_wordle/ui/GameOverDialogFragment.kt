package com.example.poke_wordle.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.poke_wordle.R
import com.example.poke_wordle.databinding.FragmentWinLooseBinding


class GameOverDialogFragment(private val hasWon :Boolean) : DialogFragment() {
    private lateinit var binding: FragmentWinLooseBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWinLooseBinding.inflate(layoutInflater)
        if (hasWon) {
            binding.winLooseIV.setBackgroundResource(R.drawable.win_dialog)
        } else {
            binding.winLooseIV.setBackgroundResource(R.drawable.loose_dialog)
        }
        return binding.root
    }

}