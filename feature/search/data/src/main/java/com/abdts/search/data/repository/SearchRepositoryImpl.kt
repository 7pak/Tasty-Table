package com.abdts.search.data.repository

import com.abdts.search.data.data_source.local.RecipeDao
import com.abdts.search.data.data_source.remote.SearchApi
import com.abdts.search.data.mapper.toDomain
import com.abdts.search.data.mapper.toRecipeDomain
import com.abdts.search.data.model.RecipeCategoryModel
import com.abdts.search.data.model.RecipeDto
import com.abdts.search.data.model.RecipeModel
import com.abdts.search.domain.models.Recipe
import com.abdts.search.domain.models.RecipeCategory
import com.abdts.search.domain.models.RecipeDetails
import com.abdts.search.domain.repository.SearchRepository
import io.ktor.client.call.body
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.Flow

class SearchRepositoryImpl(
    private val searchApi: SearchApi,
    private val recipeDao: RecipeDao
): SearchRepository {
    override suspend fun getRecipe(query:String): Result<List<Recipe>> {
        return try{
            val response = searchApi.getRecipes(query = query)

             if (response.status.value in 200..299) {
                val body = response.body<RecipeModel<RecipeDto>>().recipes
                if (body.isNullOrEmpty()) {
                    Result.failure(Exception("No recipes found"))
                } else {
                    Result.success(body.toRecipeDomain())
                }
            } else {
                Result.failure(Exception("Error occurred: ${response.status.description}"))
            }
        }catch (e:IOException){
            Result.failure(Exception("Lost connection: Check your internet"))
        }
        catch (e:Exception){
            Result.failure(Exception("Error ${e.message.toString()}"))
        }
    }

    override suspend fun getRecipeDetails(id: String): Result<RecipeDetails> {
        return try{
            val response = searchApi.getRecipeDetails(id = id)

             if (response.status.value in 200..299) {
                val body = response.body<RecipeModel<RecipeDto>>().recipes
                if (body.isNullOrEmpty()) {
                    Result.failure(Exception("Error occurred: Empty body"))
                } else {
                    Result.success(body.first().toDomain())
                }
            } else {
                 Result.failure(Exception("Error occurred: ${response.status.description}"))
            }
        }catch (e:IOException){
            Result.failure(Exception("Lost connection: Check your internet"))
        }
        catch (e:Exception){
            Result.failure(Exception("Error ${e.message.toString()}"))
        }
    }

    override suspend fun getCategoryRecipes(category: String):  Result<List<RecipeCategory>> {
        return try{
            val response = searchApi.getCategoryRecipes(category = category)

            if (response.status.value in 200..299) {
                val body = response.body<RecipeModel<RecipeCategoryModel>>().recipes
                if (body.isNullOrEmpty()) {
                    Result.failure(Exception("No recipes found in this category"))
                } else {
                    Result.success(body.toDomain())
                }
            } else {
                Result.failure(Exception("Error occurred: ${response.status.description}"))
            }
        }catch (e:IOException){
            Result.failure(Exception("Lost connection: Check your internet"))
        }
        catch (e:Exception){
            Result.failure(Exception("Error ${e.message.toString()}"))
        }
    }

    override suspend fun insertRecipe(recipe: Recipe) {
        recipeDao.insertRecipe(recipe)
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        recipeDao.deleteRecipe(recipe)
    }

    override fun getAllRecipes(): Flow<List<Recipe>> {
        return recipeDao.getAllRecipes()
    }
}