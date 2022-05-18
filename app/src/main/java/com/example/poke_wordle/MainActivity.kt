package com.example.poke_wordle

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.poke_wordle.databinding.ActivityMainBinding
import com.example.poke_wordle.db.AppDatabase
import com.example.poke_wordle.db.Pokemon
import com.example.poke_wordle.network.PokemonList
import com.example.poke_wordle.network.PokemonService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // TODO("SEED DB ONCREATE ROOM")
        // getPokemonList()
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, throwable ->
        Log.e("EXCEPTION HANDLER", throwable.cause.toString())
        throwable.message?.let { Log.e("EXCEPTION HANDLER", it) }
        throwable.printStackTrace()
    }

    private fun getPokemonList() {
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            val call = PokemonService.create().getPokemonList()
            val pokemonList = call.body()?.toEntityList()
            if (call.isSuccessful) {
                if (pokemonList != null) {
                    AppDatabase.getInstance(applicationContext).pokemonDao().insertAll(pokemonList)
                }
                runOnUiThread {
                    showSuccess()
                }
            } else {
                runOnUiThread {
                    showError()
                }
            }
        }
    }

    private fun showError() {
        Toast.makeText(this,"Error al buscar pokemones",Toast.LENGTH_SHORT).show()
    }

    private fun showSuccess() {
        Toast.makeText(this,"Se agregaron Los pokemons a DB",Toast.LENGTH_SHORT).show()
    }

}

private fun PokemonList.toEntityList(): List<Pokemon> {
    return this.results.map {
        Pokemon(
            it.url.split('/').dropLast(1).last().toInt(),
            it.name,
            it.url
        )
    }
}
