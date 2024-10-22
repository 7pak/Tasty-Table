package com.abdts.search.data.repository

import com.abdts.search.data.data_source.local.RecipeDao
import com.abdts.search.data.data_source.remote.SearchApi
import com.abdts.search.data.mapper.toDomain
import com.abdts.search.data.mapper.toRecipeDomain
import com.abdts.search.data.model.RecipeDto
import com.abdts.search.data.model.RecipeModel
import com.abdts.search.domain.models.Recipe
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SearchRepositoryImplTest {

    private val searchApi: SearchApi = mockk()
    private val recipeDao: RecipeDao = mockk()

    @Test
    fun test_success() = runTest {
        val mockHttpResponse: HttpResponse = mockk {
            coEvery { status } returns HttpStatusCode.OK
            coEvery { body<RecipeModel<RecipeDto>>().recipes } returns getMockRecipes().getOrThrow()
        }

        coEvery { searchApi.getRecipes("rice") } returns mockHttpResponse

        val searchRep = SearchRepositoryImpl(searchApi, recipeDao)

        val response = searchRep.getRecipe("rice")

        assertEquals(getMockRecipes().getOrThrow().toRecipeDomain(), response.getOrThrow())
    }

    @Test
    fun test_emptyOrNull_success() = runTest {
        val mockHttpResponse: HttpResponse = mockk {
            coEvery { status } returns HttpStatusCode.OK
            coEvery { body<RecipeModel<RecipeDto>>().recipes } returns emptyList()
        }

        coEvery { searchApi.getRecipes("rice") } returns mockHttpResponse

        val searchRep = SearchRepositoryImpl(searchApi, recipeDao)

        val response = searchRep.getRecipe("rice")

        val message = "No recipes found"
        assertEquals(message, response.exceptionOrNull()?.message)
    }
    @Test
    fun test_failure() = runTest {
        val mockHttpResponse: HttpResponse = mockk {
            coEvery { status } returns HttpStatusCode.BadRequest

            coEvery { body<RecipeModel<RecipeDto>>().recipes } returns emptyList()
        }

        coEvery { searchApi.getRecipes("rice") } returns mockHttpResponse

        val searchRep = SearchRepositoryImpl(searchApi, recipeDao)

        val response = searchRep.getRecipe("rice")

        val expectedMessage = "Error occurred: Bad Request"

        assertEquals(expectedMessage, response.exceptionOrNull()?.message)
    }


    //Recipe detail test cases::


    @Test
    fun test_success_recipe_details() = runTest {
        val mockHttpResponse: HttpResponse = mockk {
            coEvery { status } returns HttpStatusCode.OK
            coEvery { body<RecipeModel<RecipeDto>>().recipes } returns getMockRecipes().getOrThrow()
        }

        coEvery { searchApi.getRecipeDetails("id") } returns mockHttpResponse

        val searchRep = SearchRepositoryImpl(searchApi, recipeDao)

        val response = searchRep.getRecipeDetails("id")

        assertEquals(getMockRecipes().getOrThrow().first().toDomain(), response.getOrThrow())
    }

    @Test
    fun test_emptyOrNull_success_recipe_details() = runTest {
        val mockHttpResponse: HttpResponse = mockk {
            coEvery { status } returns HttpStatusCode.OK
            coEvery { body<RecipeModel<RecipeDto>>().recipes } returns emptyList()
        }

        coEvery { searchApi.getRecipeDetails("id") } returns mockHttpResponse

        val searchRep = SearchRepositoryImpl(searchApi, recipeDao)

        val response = searchRep.getRecipeDetails("id")

        val expectedMessage = "Error occurred: Empty body"
        assertEquals(expectedMessage, response.exceptionOrNull()?.message)
    }


    @Test
    fun test_recipe_details_failure() = runTest {
        val mockHttpResponse: HttpResponse = mockk {
            coEvery { status } returns HttpStatusCode.BadRequest

            coEvery { body<RecipeModel<RecipeDto>>().recipes } returns emptyList()
        }

        coEvery { searchApi.getRecipeDetails("id") } returns mockHttpResponse

        val searchRep = SearchRepositoryImpl(searchApi, recipeDao)

        val response = searchRep.getRecipeDetails("id")

        val expectedMessage = "Error occurred: Bad Request"

        assertEquals(expectedMessage, response.exceptionOrNull()?.message)
    }

    //Test local database

    @Test
    fun test_insert() = runTest{
        val searchRep = SearchRepositoryImpl(searchApi, FakeRecipeDao())
        val recipe = getMockRecipes().getOrThrow().toRecipeDomain().first()
        searchRep.insertRecipe(recipe)

        assertEquals(recipe,searchRep.getAllRecipes().first().first())
    }

    @Test
    fun test_delete() = runTest {
        val searchRep = SearchRepositoryImpl(searchApi, FakeRecipeDao())
        val recipe = getMockRecipes().getOrThrow().toRecipeDomain().first()
        searchRep.insertRecipe(recipe)
        val list = searchRep.getAllRecipes().first().first()
        assertEquals(recipe,list)

        searchRep.deleteRecipe(recipe)

        assertEquals(emptyList<Recipe>(),searchRep.getAllRecipes().last())
    }




    private fun getMockRecipes(): Result<List<RecipeDto>> =
        Result.success(
            listOf(
                RecipeDto(
                    dateModified = null,
                    idMeal = "idMeal",
                    strArea = "Uae",
                    strCategory = "category",
                    strYoutube = "strYoutube",
                    strTags = "tag1,tag2",
                    strMeal = "Rice",
                    strSource = "strSource",
                    strMealThumb = "strMealThumb",
                    strInstructions = "strInstructions",
                    strCreativeCommonsConfirmed = null,
                    strIngredient1 = null,
                    strIngredient2 = null,
                    strIngredient3 = null,
                    strIngredient4 = null,
                    strIngredient5 = null,
                    strIngredient6 = null,
                    strIngredient7 = null,
                    strIngredient8 = null,
                    strIngredient9 = null,
                    strIngredient10 = null,
                    strIngredient11 = null,
                    strIngredient12 = null,
                    strIngredient13 = null,
                    strIngredient14 = null,
                    strIngredient15 = null,
                    strIngredient16 = null,
                    strIngredient17 = null,
                    strIngredient18 = null,
                    strIngredient19 = null,
                    strIngredient20 = null,
                    strMeasure1 = null,
                    strMeasure2 = null,
                    strMeasure3 = null,
                    strMeasure4 = null,
                    strMeasure5 = null,
                    strMeasure6 = null,
                    strMeasure7 = null,
                    strMeasure8 = null,
                    strMeasure9 = null,
                    strMeasure10 = null,
                    strMeasure11 = null,
                    strMeasure12 = null,
                    strMeasure13 = null,
                    strMeasure14 = null,
                    strMeasure15 = null,
                    strMeasure16 = null,
                    strMeasure17 = null,
                    strMeasure18 = null,
                    strMeasure19 = null,
                    strMeasure20 = null,
                    strDrinkAlternate = null,
                    strImageSource = "empty"
                )
            )
        )
}
