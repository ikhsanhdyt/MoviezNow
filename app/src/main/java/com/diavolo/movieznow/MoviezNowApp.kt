package com.diavolo.movieznow

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.diavolo.movieznow.di.mainModule
import com.diavolo.movieznow.di.movieListModule
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MoviezNowApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AndroidThreeTen.init(this)

        startKoin {
            androidContext(this@MoviezNowApp)
            modules(mainModule, movieListModule)
        }
        setupTimberLog()
    }

    private fun setupTimberLog() {
        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String {
                return String.format(
                    "%s::%s:%s",
                    super.createStackElementTag(element),
                    element.methodName,
                    element.lineNumber
                )
            }
        })
    }
}