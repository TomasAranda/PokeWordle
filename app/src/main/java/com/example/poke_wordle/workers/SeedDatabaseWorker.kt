package com.example.poke_wordle.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.poke_wordle.db.AppDatabase
import com.example.poke_wordle.db.model.PokemonEntity
import com.example.poke_wordle.network.PokemonList
import com.example.poke_wordle.network.PokemonService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SeedDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val call = PokemonService.create().getPokemonList()
            val pokemonList = call.body()?.toEntityList()
            if (call.isSuccessful) {
                if (pokemonList != null) {
                    AppDatabase.getInstance(applicationContext).pokemonDao().insertAll(pokemonList)
                }
                Result.success()

            } else {
                Log.e("SeedDatabaseWorker", "Error seeding database - network call unsuccessful")
                Result.failure()
            }
        } catch (ex: Exception) {
            Log.e("SeedDatabaseWorker", "Error seeding database", ex)
            Result.failure()
        }
    }
}

private fun PokemonList.toEntityList(): List<PokemonEntity> {
    return this.results.map {
        PokemonEntity(
            it.url.split('/').dropLast(1).last().toInt(),
            it.name,
            it.url
        )
    }
}