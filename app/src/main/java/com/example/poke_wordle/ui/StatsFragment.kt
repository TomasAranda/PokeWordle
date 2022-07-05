package com.example.poke_wordle.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.poke_wordle.BuildConfig
import com.example.poke_wordle.databinding.FragmentStatsBinding
import com.example.poke_wordle.ui.viewmodel.StatsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.sqrt

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
            binding.winPercentage.text = "${it}%"
        }
        model.winPercentagesByAttempt.observe(viewLifecycleOwner) {
            setStatChartViews(it)
        }

        return binding.root
    }

    private fun setStatChartViews(percentages: List<Double>) {
        for ((index, percentage) in percentages.withIndex()) {
            val chartBarResource = resources.getIdentifier("chart_bar_${index + 1}", "id",
                BuildConfig.APPLICATION_ID
            )
            val chartBarView = view?.findViewById<TextView>(chartBarResource)
            if (percentage > 0.0) {
                chartBarView?.text = "${percentage}%"
                chartBarView?.setBackgroundColor(Color.parseColor("#6aaa64"))
            } else {
                chartBarView?.text = "0%"
            }
            val currentWidth = chartBarView?.width
            val layoutParams = chartBarView?.layoutParams
            if (percentage > 5.0) {
                val newWidth = (currentWidth!!.toDouble() * sqrt(percentage / 100)).toInt()
                layoutParams?.width = newWidth
                chartBarView.layoutParams = layoutParams
            } else {
                layoutParams?.width = ViewGroup.LayoutParams.WRAP_CONTENT
                chartBarView?.layoutParams = layoutParams
            }
        }
    }

}