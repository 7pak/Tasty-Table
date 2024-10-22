package com.abdts.search.ui.screens.favorite

import com.abdts.search.domain.models.Recipe
import com.abdts.search.domain.use_cases.DeleteRecipe
import com.abdts.search.domain.use_cases.GetAllRecipesLocally
import com.abdts.search.ui.screens.recipe_list.RecipeListModelTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoriteModelTest {

    @get:Rule(order = 1)
    val mainDispatcherRule = RecipeListModelTest.MainDispatcherRule()

    private var mockkGetAllRecipesLocally = mockk<GetAllRecipesLocally>()

    @Before
    fun setUp() {
        mockkGetAllRecipesLocally = mockk {
            coEvery { this@mockk.invoke() } returns flowOf(getMockRecipes())
        }
    }

    @Test
    fun test_alpha_sort() = runTest {

        val viewModel = FavoriteModel(mockkGetAllRecipesLocally, mockk())

        viewModel.onEvent(FavoriteScreen.Event.AlphabeticalSort)

        assertEquals(
            getMockRecipes().sortedBy { it.strMeal },
            viewModel.uiState.value.data
        )
    }

    @Test
    fun test_lessIngredient_sort() = runTest {

        val viewModel = FavoriteModel(mockkGetAllRecipesLocally, mockk())

        viewModel.onEvent(FavoriteScreen.Event.LessIngredientSort)

        assertEquals(
            getMockRecipes().sortedBy { it.strInstruction.length },
            viewModel.uiState.value.data
        )
    }

    @Test
    fun test_reset_sort() = runTest {

        val viewModel = FavoriteModel(mockkGetAllRecipesLocally, mockk())

        viewModel.onEvent(FavoriteScreen.Event.AlphabeticalSort)

        assertEquals(
            getMockRecipes().sortedBy { it.strMeal },
            viewModel.uiState.value.data
        )

        viewModel.onEvent(FavoriteScreen.Event.ResetSort)

        assertEquals(
            getMockRecipes(),
            viewModel.uiState.value.data
        )
    }

    @Test
    fun test_delete() = runTest {

        val list = getMockRecipes().toMutableList()

        val mockkDeleteRecipe: DeleteRecipe = mockk {
            coEvery {
                this@mockk.invoke(recipe = list.first())
            } returns flow { list.remove(list.first()) }
        }


        val viewModel = FavoriteModel(mockkGetAllRecipesLocally, mockkDeleteRecipe)

        viewModel.onEvent(FavoriteScreen.Event.DeleteRecipe(getMockRecipes().first()))
        assert(list.isEmpty())
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