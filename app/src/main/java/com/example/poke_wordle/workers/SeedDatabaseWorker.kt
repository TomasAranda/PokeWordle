package com.example.poke_wordle.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.poke_wordle.data.db.AppDatabase
import com.example.poke_wordle.data.db.model.PokemonEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SeedDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val filename = inputData.getString("data-filename")
            if (filename != null) {
                applicationContext.assets.open(filename).use { inputStream ->
                    JsonReader(inputStream.reader()).use { jsonReader ->
                        val pokemonType = object : TypeToken<List<PokemonEntity>>() {}.type
                        val pokemonList: List<PokemonEntity> = Gson().fromJson(jsonReader, pokemonType)

                        val database = AppDatabase.getInstance(applicationContext)
                        database.pokemonDao().insertAll(pokemonList)

                        Result.success()
                    }
                }
            } else {
                Log.e("SeedDatabaseWorker", "Error seeding database - no valid filename")
                Result.failure()
            }
        } catch (ex: Exception) {
            Log.e("SeedDatabaseWorker", "Error seeding database", ex)
            Result.failure()
        }
    }
}