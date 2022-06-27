package com.example.poke_wordle

import android.app.Application
import com.example.poke_wordle.di.wordleModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WordleApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        // start Koin context
        startKoin {
            androidLogger()
            androidContext(this@WordleApplication)
            modules(wordleModule)
        }
    }
}