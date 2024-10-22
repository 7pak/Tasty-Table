package com.abdts.search.domain.repository

import com.abdts.search.domain.models.Recipe
import com.abdts.search.domain.models.RecipeCategory
import com.abdts.search.domain.models.RecipeDetails
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun getRecipe(query:String):Result<List<Recipe>>

    suspend fun getRecipeDetails(id:String):Result<RecipeDetails>

    suspend fun getCategoryRecipes(category:String):Result<List<RecipeCategory>>

    suspend fun insertRecipe(recipe: Recipe)

     suspend fun deleteRecipe(recipe: Recipe)

     fun getAllRecipes():Flow<List<Recipe>>



}