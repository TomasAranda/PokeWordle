package com.example.poke_wordle.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.poke_wordle.databinding.ActivityMainBinding
import com.example.poke_wordle.ui.viewmodel.PokeWordleViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val wordleViewModel by viewModel<PokeWordleViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
