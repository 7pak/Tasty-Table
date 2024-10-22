package com.abdts.search.ui.screens.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdts.common.utils.UiText
import com.abdts.search.domain.models.Recipe
import com.abdts.search.domain.use_cases.DeleteRecipe
import com.abdts.search.domain.use_cases.GetAllRecipesLocally

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoriteModel(
    private val getAllRecipesLocally: GetAllRecipesLocally,
    private val deleteRecipe: DeleteRecipe
) : ViewModel() {
    private val _uiState = MutableStateFlow(FavoriteScreen.UiState())
    val uiState: StateFlow<FavoriteScreen.UiState> get() = _uiState.asStateFlow()

    private var originalList = mutableListOf<Recipe>()

    private val _navigation = Channel<FavoriteScreen.Navigation>()
    val navigation: Flow<FavoriteScreen.Navigation> = _navigation.receiveAsFlow()

    init {
        getRecipesList()
    }

    private fun getRecipesList() = viewModelScope.launch {
        getAllRecipesLocally.invoke().collectLatest { recipeList ->
            originalList = recipeList.toMutableList()
            _uiState.update {
                FavoriteScreen.UiState(data = recipeList.reversed())
            }
        }
    }

    fun onEvent(event: FavoriteScreen.Event) {
        when (event) {
            FavoriteScreen.Event.AlphabeticalSort -> alphabeticalSort()
            FavoriteScreen.Event.LessIngredientSort -> lessIngredientSort()
            FavoriteScreen.Event.ResetSort -> resetSort()
            is FavoriteScreen.Event.ShowDetails -> viewModelScope.launch {
                _navigation.send(FavoriteScreen.Navigation.GotoRecipeDetailScreen(event.id))
            }

            is FavoriteScreen.Event.DeleteRecipe -> {
                deleteRecipe.invoke(event.recipe).launchIn(viewModelScope)
            }

            is FavoriteScreen.Event.GoToDetails ->  viewModelScope.launch {
                _navigation.send(FavoriteScreen.Navigation.GotoRecipeDetailScreen(event.id))
            }
        }
    }

    private fun alphabeticalSort() = _uiState.update {
        FavoriteScreen.UiState(data = originalList.sortedBy {
            it.strMeal
        })
    }
    private fun lessIngredientSort() = _uiState.update {
        FavoriteScreen.UiState(data = originalList.sortedBy {
            it.strInstruction.length
        })
    }

    private fun resetSort() = _uiState.update {
        FavoriteScreen.UiState(data = originalList.reversed() )
    }
}


object FavoriteScreen {
    data class UiState(
        val isLoading: Boolean = false,
        val data: List<Recipe>? = null,
        val error: UiText = UiText.Idle
    )

    sealed interface Navigation {
        data class GotoRecipeDetailScreen(val id: String):Navigation
    }

    sealed interface Event {
        data object AlphabeticalSort : Event
        data object LessIngredientSort : Event
        data object ResetSort : Event
        data class ShowDetails(val id: String) : Event
        data class DeleteRecipe(val recipe: Recipe) : Event
        data class GoToDetails(val id: String) : Event
    }
}