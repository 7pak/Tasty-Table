package com.abdts.recipeapp.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.abdts.search.data.data_source.local.RecipeDao
import com.abdts.search.domain.models.Recipe

@Database(entities = [Recipe::class], version = 1, exportSchema = false)
abstract class AppDatabase:RoomDatabase(){

    abstract fun getRecipeDao():RecipeDao

    companion object{
        fun getInstanse(context: Context) = Room.databaseBuilder(context,AppDatabase::class.java,"recipe_app_dp")
            .fallbackToDestructiveMigration()
            .build()
    }
}