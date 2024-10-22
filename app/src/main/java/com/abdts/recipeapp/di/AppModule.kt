package com.abdts.recipeapp.di

import com.abdts.recipeapp.local.AppDatabase
import com.abdts.recipeapp.navigation.NavigationSubGraph
import com.abdts.search.data.data_source.local.RecipeDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single {
        NavigationSubGraph(searchFeatureApi = get(), mediaPlayerFeatureApi = get())
    }

    single <AppDatabase>{
        AppDatabase.getInstanse(androidApplication())
    }

    single <RecipeDao>{
        get<AppDatabase>().getRecipeDao()
    }
}