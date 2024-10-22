package com.abdts.search.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdts.common.utils.NetworkResult
import com.abdts.common.utils.UiText
import com.abdts.search.domain.models.Recipe
import com.abdts.search.domain.models.RecipeDetails
import com.abdts.search.domain.use_cases.DeleteRecipe
import com.abdts.search.domain.use_cases.GetRecipeDetails
import com.abdts.search.domain.use_cases.InsertRecipe
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeDetailModel(
    private val getRecipeDetails: GetRecipeDetails,
    private val insertRecipe: InsertRecipe,
    private val deleteRecipe: DeleteRecipe,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeDetail.UiState())
    val uiState: StateFlow<RecipeDetail.UiState> = _uiState.asStateFlow()

    private val _navigation = Channel<RecipeDetail.Navigation>()
    val navigation: Flow<RecipeDetail.Navigation> = _navigation.receiveAsFlow()

    private fun recipeDetails(id: String) = getRecipeDetails.invoke(id).onEach { result ->
        when (result) {
            is NetworkResult.Error -> {
                _uiState.update {
                    RecipeDetail.UiState(error = UiText.RemoteString(result.message.toString()))
                }
            }

            is NetworkResult.Loading -> {
                _uiState.update {
                    RecipeDetail.UiState(isLoading = true)
                }
            }

            is NetworkResult.Success -> {
                _uiState.update {
                    RecipeDetail.UiState(data = result.data)
                }
            }
        }

    }.launchIn(viewModelScope)

    fun onEvent(event: RecipeDetail.Event) {
        when (event) {
            is RecipeDetail.Event.FetchRecipeDetails -> {
                recipeDetails(event.id)
            }

            RecipeDetail.Event.GoToRecipeListScreen -> viewModelScope.launch {
                _navigation.send(RecipeDetail.Navigation.GoToRecipeListScreen)
            }

            is RecipeDetail.Event.InsertRecipe -> {
                insertRecipe.invoke(event.recipeDetails.toRecipe()).launchIn(viewModelScope)
            }

            is RecipeDetail.Event.DeleteRecipe ->{
                deleteRecipe.invoke(event.recipeDetails.toRecipe()).launchIn(viewModelScope)
            }

            is RecipeDetail.Event.GoToMediaPlayer -> viewModelScope.launch {
                _navigation.send(RecipeDetail.Navigation.GoToMediaPlayer(event.videoUrl))
            }
        }
    }

}

fun RecipeDetails.toRecipe(): Recipe {
    return Recipe(
        idMeal,
        strArea,
        strMeal,
        strMealThumb,
        strCategory,
        strTags,
        strYouTube,
        strInstruction
    )
}

object RecipeDetail {
    data class UiState(
        val isLoading: Boolean = false,
        val error: UiText = UiText.Idle,
        val data: RecipeDetails? = null
    )

    sealed interface Navigation {

        data object GoToRecipeListScreen : Navigation
        data class GoToMediaPlayer(val videoUrl:String):Navigation
    }

    sealed interface Event {
        data class FetchRecipeDetails(val id: String) : Event
        data object GoToRecipeListScreen : Event
        data class InsertRecipe(val recipeDetails: RecipeDetails) : Event
        data class DeleteRecipe(val recipeDetails: RecipeDetails) : Event
        data class GoToMediaPlayer(val videoUrl:String):Event
    }
}