package com.example.poke_wordle.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.poke_wordle.data.db.AppDatabase
import com.example.poke_wordle.databinding.FragmentStatsBinding
import kotlinx.coroutines.launch

class StatsFragment : Fragment() {
    private lateinit var binding: FragmentStatsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsBinding.inflate(inflater)

        lifecycleScope.launch {
            val wordleDao = AppDatabase.getInstance(requireContext()).pokeWordlePlayDao()
            binding.playCount.text = wordleDao.getWordlePlayCount().toString()
            binding.winPercentage.text = wordleDao.getWordlePlayWinPercentage().toString()
            binding.attemptsPercentage.text = wordleDao.getWordleAttemptsPercentages().toString()

        }
        return binding.root
    }

}