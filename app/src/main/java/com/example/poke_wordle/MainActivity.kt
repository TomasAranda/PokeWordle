package com.example.poke_wordle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.poke_wordle.databinding.ActivityMainBinding
import com.example.poke_wordle.db.AppDatabase
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        GlobalScope.launch(Dispatchers.IO) {
            AppDatabase.getInstance(applicationContext).pokemonDao().getRandomPokemonFromList()
        }
        setContentView(binding.root)
    }
}
