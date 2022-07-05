package com.example.poke_wordle.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poke_wordle.data.repository.PokeWordlePlayRepository
import kotlinx.coroutines.launch

class StatsViewModel(
    private val pokeWordlePlayRepository: PokeWordlePlayRepository
): ViewModel() {

    init {
        viewModelScope.launch {
            val count = pokeWordlePlayRepository.getCount()
            val winPercentages = pokeWordlePlayRepository.getWinPercentage()
            val winPercentagesByAttempts = pokeWordlePlayRepository.getWinPercentagesByAttempts()
            _playCount.value = count
            _winPercentage.value = winPercentages
            _winPercentagesByAttempt.value = winPercentagesByAttempts
        }
    }

    private val _playCount = MutableLiveData<Int>(0)
    val playCount: LiveData<Int> = _playCount

    private val _winPercentage = MutableLiveData<Double>(0.0)
    val winPercentage: LiveData<Double> = _winPercentage

    private val _winPercentagesByAttempt = MutableLiveData<List<Double>>()
    val winPercentagesByAttempt: LiveData<List<Double>> = _winPercentagesByAttempt
}