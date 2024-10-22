package com.abdts.search.ui.screens.recipe_list

import com.abdts.common.utils.NetworkResult
import com.abdts.common.utils.UiText
import com.abdts.search.domain.models.Recipe
import com.abdts.search.domain.use_cases.GetAllRecipesRemotely
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description


class RecipeListModelTest{

    @get:Rule(order = 1)
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun test_success() = runTest{
        val mockkGetAllRecipes: GetAllRecipesRemotely = mockk{
            coEvery { invoke(query = "rice") } returns flowOf(NetworkResult.Success(data = getMockRecipes()))
        }

        val viewModel = RecipeListModel(mockkGetAllRecipes)
         viewModel.onEvent(RecipeList.Event.SearchRecipe("rice"))
        assertEquals(getMockRecipes(),viewModel.uiState.value.data)
    }

    @Test
    fun test_failure() = runTest {
        val mockkGetAllRecipes: GetAllRecipesRemotely = mockk{
            coEvery { invoke(query = "rice") } returns flowOf(NetworkResult.Error(message = "Error occurred"))
        }

        val viewModel = RecipeListModel(mockkGetAllRecipes)
        viewModel.onEvent(RecipeList.Event.SearchRecipe("rice"))

        val expectedMessage = "Error occurred"
        assertEquals(UiText.RemoteString(expectedMessage),viewModel.uiState.value.error)

    }



    class MainDispatcherRule(private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()):
        TestWatcher() {
        override fun starting(description: Description?) {
            Dispatchers.setMain(testDispatcher)
        }

        override fun finished(description: Description?) {
            Dispatchers.resetMain()
        }
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