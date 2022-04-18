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
     lateinit var binding:ActivityMainBinding
     private lateinit var adapter: PokeAdapter
     private val pokemonImage = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.svPoke.setOnQueryTextListener(this)
        initRecyclerView()
    }

     private fun initRecyclerView() {
         adapter = PokeAdapter(pokemonImage)
         binding.rvPokemon.layoutManager = LinearLayoutManager(this)
         binding.rvPokemon.adapter = adapter
    }

     private fun getRetrofit():Retrofit{
      return Retrofit.Builder()
          //.baseUrl("https://pokeapi.co/api/v2/")
          .baseUrl("https://dog.ceo/api/breed/")
          .addConverterFactory(GsonConverterFactory.create())
          .build()
    }
    //Se crea corutina para ejecutar todo dentro de un hilo secundario
    private fun searchFunc(query:String){
        //todo dentro del lauch este queda en un hilo secundario
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java).getPokemon("$query/images")
            val pokes = call.body()
            runOnUiThread{
                if (call.isSuccessful){
                   val imagePokemon = pokes?.pokemon ?: emptyList()
                    pokemonImage.clear()
                    pokemonImage.addAll(imagePokemon)
                    adapter.notifyDataSetChanged()
                }
                else{
                    showError()
                }
            }


        }
    }

    private fun showError() {
        Toast.makeText(this,"Paso Algo",Toast.LENGTH_SHORT).show()
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