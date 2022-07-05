package com.example.poke_wordle.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.poke_wordle.data.db.AppDatabase
import com.example.poke_wordle.data.repository.PokeWordlePlayRepository
import com.example.poke_wordle.databinding.FragmentStatsBinding
import com.example.poke_wordle.ui.viewmodel.StatsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatsFragment : Fragment() {
    private lateinit var binding: FragmentStatsBinding
    private val model by viewModel<StatsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsBinding.inflate(inflater)

        model.playCount.observe(viewLifecycleOwner) {
            binding.playCount.text = it.toString()
        }
        model.winPercentage.observe(viewLifecycleOwner) {
            binding.winPercentage.text = (100.00 * it).toString() + "%"
        }
        model.winPercentagesByAttempt.observe(viewLifecycleOwner) {
            binding.attemptsPercentage.text = it.map { percentage -> (percentage * 100.00).toString() + "%" }.toString()
        }

        return binding.root
    }

}