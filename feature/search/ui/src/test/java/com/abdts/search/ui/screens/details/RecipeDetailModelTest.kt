package com.abdts.search.ui.screens.details

import com.abdts.common.utils.NetworkResult
import com.abdts.common.utils.UiText
import com.abdts.search.domain.models.Recipe
import com.abdts.search.domain.models.RecipeDetails
import com.abdts.search.domain.use_cases.DeleteRecipe
import com.abdts.search.domain.use_cases.GetRecipeDetails
import com.abdts.search.domain.use_cases.InsertRecipe
import com.abdts.search.ui.screens.recipe_list.RecipeListModelTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class RecipeDetailModelTest{

    @get:Rule(order = 1)
    val mainDispatcherRule = RecipeListModelTest.MainDispatcherRule()


    @Test
    fun test_success(){
        val mockkGetRecipeDetails:GetRecipeDetails = mockk{
            coEvery { invoke(id = "id") } returns flowOf(NetworkResult.Success(getMockRecipeDetails().first()))
        }

        val viewModel = RecipeDetailModel(getRecipeDetails = mockkGetRecipeDetails, insertRecipe = mockk(), deleteRecipe = mockk())

        viewModel.onEvent(RecipeDetail.Event.FetchRecipeDetails("id"))

        assertEquals(getMockRecipeDetails().first(),viewModel.uiState.value.data)
    }

    @Test
    fun test_failure() = runTest {
        val mockkGetRecipeDetails:GetRecipeDetails = mockk{
            coEvery { invoke(id = "id") } returns flowOf(NetworkResult.Error(message = "Error occurred"))
        }

        val viewModel = RecipeDetailModel(getRecipeDetails = mockkGetRecipeDetails, insertRecipe = mockk(), deleteRecipe = mockk())

        viewModel.onEvent(RecipeDetail.Event.FetchRecipeDetails("id"))

        val expectedMessage = "Error occurred"
        assertEquals(UiText.RemoteString(expectedMessage),viewModel.uiState.value.error)

    }

    @Test
    fun test_insertion() = runTest {

        val list = mutableListOf<Recipe>()

        val mockkInsertRecipe:InsertRecipe = mockk{
            coEvery { this@mockk.invoke(recipe = getMockRecipeDetails().first().toRecipe())
            } returns flowOf(Unit)

            list.add(getMockRecipeDetails().first().toRecipe())
        }
        val viewModel = RecipeDetailModel(getRecipeDetails = mockk(), insertRecipe = mockkInsertRecipe, deleteRecipe = mockk())

        viewModel.onEvent(RecipeDetail.Event.InsertRecipe(getMockRecipeDetails().first()))

        assert(list.contains(getMockRecipeDetails().first().toRecipe()))

    }
    @Test
    fun test_deletion() = runTest {

        val list = mutableListOf<Recipe>()

        val mockkInsertRecipe:InsertRecipe = mockk{
            coEvery { this@mockk.invoke(recipe = getMockRecipeDetails().first().toRecipe())
            } returns flowOf(Unit).also {
                list.add(getMockRecipeDetails().first().toRecipe())
            }

        }
        val mockkDeleteRecipe:DeleteRecipe = mockk{
            coEvery { this@mockk.invoke(recipe = getMockRecipeDetails().first().toRecipe())
            } returns flowOf(Unit).also {
                list.remove(getMockRecipeDetails().first().toRecipe())
            }


        }


        val viewModel = RecipeDetailModel(getRecipeDetails = mockk(), insertRecipe = mockkInsertRecipe, deleteRecipe = mockkDeleteRecipe)

        viewModel.onEvent(RecipeDetail.Event.InsertRecipe(getMockRecipeDetails().first()))

        viewModel.onEvent(RecipeDetail.Event.DeleteRecipe(getMockRecipeDetails().first()))

        assert(list.isEmpty())

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