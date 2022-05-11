package com.example.tp_mobile

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tp_mobile.databinding.ActivityMainBinding
import com.example.tp_mobile.db.AppDatabase
import com.example.tp_mobile.network.PokemonList
import com.example.tp_mobile.network.PokemonService
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getPokemonList()
    }

     private fun getRetrofit(): Retrofit {
         val gson = GsonBuilder().setLenient().create()
         return Retrofit.Builder()
          .baseUrl("https://pokeapi.co/api/v2/")
          .addConverterFactory(GsonConverterFactory.create(gson))
          .build()
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, throwable ->
        throwable.message?.let { Log.e("EXCEPTION HANDLER", it) }
        throwable.printStackTrace()
    }

    // Crea corutina para ejecutar la busqueda dentro de un hilo secundario
    private fun getPokemonList() {
        // La llamada a la API dentro del lauch queda en un hilo secundario
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            val call = getRetrofit().create(PokemonService::class.java).getPokemonList()
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

private fun PokemonList.toEntityList(): List<com.example.tp_mobile.db.PokemonFromList> {
    return this.results.map {
        com.example.tp_mobile.db.PokemonFromList(
            it.url.split('/').dropLast(1).last().toInt(),
            it.name,
            it.url
        )
    }
}
