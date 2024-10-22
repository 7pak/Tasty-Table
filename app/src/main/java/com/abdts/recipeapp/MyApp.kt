package com.abdts.recipeapp

import android.app.Application
import com.abdts.di.searchFeatureModule
import com.abdts.recipeapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule,searchFeatureModule)
            androidContext(this@MyApp)
            androidLogger()
        }
    }
}