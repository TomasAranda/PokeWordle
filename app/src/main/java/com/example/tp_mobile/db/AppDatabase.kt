package com.example.tp_mobile.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [Pokemon::class]
)
abstract class AppDatabase : RoomDatabase() {

}