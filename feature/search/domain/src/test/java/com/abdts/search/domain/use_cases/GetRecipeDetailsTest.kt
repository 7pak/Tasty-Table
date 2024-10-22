package com.abdts.search.domain.use_cases

import com.abdts.search.domain.models.RecipeDetails
import com.abdts.search.domain.repository.SearchRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetRecipeDetailsTest{


    @Test
    fun test_success() = runTest {

        val mockSearchRep: SearchRepository = mockk {
            coEvery { getRecipeDetails("id") } returns Result.success(getMockRecipeDetails().first())
        }
        val useCase = GetRecipeDetails(mockSearchRep)
        val response =useCase.invoke("id")

        assertEquals(getMockRecipeDetails(), listOf(response.last().data))
    }

    @Test
    fun test_failure() = runTest {

        val mockSearchRep: SearchRepository = mockk {
            coEvery { getRecipeDetails("id") } returns Result.failure(Exception("error occurred"))
        }
        val useCase = GetRecipeDetails(mockSearchRep)
        val response =useCase.invoke("id")

        val expectedMessage = "error occurred"

        assertEquals(expectedMessage,response.last().message)
    }


    @Test
    fun test_exception() = runTest {

        val mockSearchRep: SearchRepository = mockk {
            coEvery { getRecipeDetails("id") } throws  RuntimeException("no connection")
        }
        val useCase = GetRecipeDetails(mockSearchRep)
        val response =useCase.invoke("id")

        val expectedMessage = "no connection"

        assertEquals(expectedMessage,response.last().message)
    }


    private fun getMockRecipeDetails(): List<RecipeDetails> =
        listOf(
            RecipeDetails(
                idMeal = "idMeal",
                strArea = "Uae",
                strCategory = "category",
                strYouTube = "strYoutube",
                strTags = "tag1,tag2",
                strMeal = "Rice",
                strMealThumb = "strMealThumb",
                strInstruction = "strInstructions",
                ingredientsPair = listOf()
            )
        )
}