package com.example.tp_mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tp_mobile.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(),SearchView.OnQueryTextListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PokeAdapter
    private val pokemonImages = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.svPoke.setOnQueryTextListener(this)
        initRecyclerView()
    }

     private fun initRecyclerView() {
         adapter = PokeAdapter(pokemonImages)
         binding.rvPokemon.layoutManager = LinearLayoutManager(this)
         binding.rvPokemon.adapter = adapter
    }

     private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
          .baseUrl("https://pokeapi.co/api/v2/pokemon/")
          .addConverterFactory(GsonConverterFactory.create())
          .build()
    }

    // Crea corutina para ejecutar la busqueda dentro de un hilo secundario
    private fun searchFunc(query: String) {
        // La llamada a la API dentro del lauch queda en un hilo secundario
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java).getPokemon(query)
            val pokemon = call.body()
            runOnUiThread{
                if (call.isSuccessful){
                    val newImage = pokemon?.sprites?.other?.officialArtwork?.frontDefault
                    if (newImage != null) {
                        pokemonImages.add(0, newImage)
                    }
                    binding.rvPokemon.adapter?.notifyItemInserted(0)
                }
                else{
                    showError()
                }
            }
        }
    }

    private fun showError() {
        Toast.makeText(this,"No se encontró el Pokemon",Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()){
            searchFunc(query.lowercase())
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}