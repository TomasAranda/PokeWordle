package com.example.poke_wordle.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate


/**
 * Type converters to allow Room to reference complex data types.
 */
class Converters {
    @TypeConverter
    fun localDateToString(date: LocalDate): String = date.toString()

    @TypeConverter
    fun stringToLocalDate(stringDate: String): LocalDate = LocalDate.parse(stringDate)

    @TypeConverter
    fun stringToMutableList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun mutableListToString(list: List<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}