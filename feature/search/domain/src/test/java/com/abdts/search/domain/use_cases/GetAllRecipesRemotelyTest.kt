package com.abdts.search.domain.use_cases

import com.abdts.search.domain.models.Recipe
import com.abdts.search.domain.repository.SearchRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetAllRecipesRemotelyTest {

    @Test
    fun test_success() = runTest {

        val mockSearchRep: SearchRepository = mockk {
            coEvery { getRecipe("rice") } returns Result.success(getMockRecipes())
        }
        val useCase = GetAllRecipesRemotely(mockSearchRep)
       val response =useCase.invoke("rice")

        assertEquals(getMockRecipes(),response.last().data)
    }

    @Test
    fun test_failure() = runTest {

        val mockSearchRep: SearchRepository = mockk {
            coEvery { getRecipe("rice") } returns Result.failure(Exception("error occurred"))
        }
        val useCase = GetAllRecipesRemotely(mockSearchRep)
       val response =useCase.invoke("rice")

        val expectedMessage = "error occurred"

        assertEquals(expectedMessage,response.last().message)
    }
    @Test
    fun test_exception() = runTest {

        val mockSearchRep: SearchRepository = mockk {
            coEvery { getRecipe("rice") } throws  RuntimeException("no connection")
        }
        val useCase = GetAllRecipesRemotely(mockSearchRep)
       val response =useCase.invoke("rice")

        val expectedMessage = "no connection"

        assertEquals(expectedMessage,response.last().message)
    }



    private fun getMockRecipes(): List<Recipe> =
            listOf(
                Recipe(
                    idMeal = "idMeal",
                    strArea = "Uae",
                    strCategory = "category",
                    strYouTube = "strYoutube",
                    strTags = "tag1,tag2",
                    strMeal = "Rice",
                    strMealThumb = "strMealThumb",
                    strInstruction = "strInstructions",
                )
            )
}