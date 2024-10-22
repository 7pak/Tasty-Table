package com.abdts.search.ui.screens.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdts.common.utils.NetworkResult
import com.abdts.common.utils.UiText
import com.abdts.search.domain.models.RecipeCategory
import com.abdts.search.domain.use_cases.GetCategoryRecipes
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

class CategoryModel(
    private val getCategoryRecipes: GetCategoryRecipes
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryRecipe.UiState())
    val uiState: StateFlow<CategoryRecipe.UiState> = _uiState.asStateFlow()

    private val _navigation = Channel<CategoryRecipe.Navigation>()
    val navigation: Flow<CategoryRecipe.Navigation> = _navigation.receiveAsFlow()

    private fun getFromCategory(category: String) =
        getCategoryRecipes.invoke(category).onEach { result ->
            when (result) {
                is NetworkResult.Error -> {
                    _uiState.update {
                        CategoryRecipe.UiState(error = UiText.RemoteString(result.message.toString()))
                    }
                }

                is NetworkResult.Loading -> {
                    _uiState.update {
                        CategoryRecipe.UiState(isLoading = true)
                    }
                }

                is NetworkResult.Success -> {
                    _uiState.update {
                        CategoryRecipe.UiState(data = result.data)
                    }
                }
            }

        }.launchIn(viewModelScope)

    fun onEvent(event: CategoryRecipe.Event) {
        when (event) {
            is CategoryRecipe.Event.FetchCategoryRecipes -> {
                getFromCategory(event.category)
            }

            is CategoryRecipe.Event.GoToDetail -> {

                viewModelScope.launch {
                    _navigation.send(CategoryRecipe.Navigation.GoToDetail(event.id))
                }
            }

            CategoryRecipe.Event.GoToHome -> {
                viewModelScope.launch {
                    _navigation.send(CategoryRecipe.Navigation.GoToHome)

                }
            }
        }
    }
}

object CategoryRecipe {

    data class UiState(
        val isLoading: Boolean = false,
        val error: UiText = UiText.Idle,
        val data: List<RecipeCategory>? = null
    )

    sealed interface Navigation {

        data class GoToDetail(val id: String) : Navigation
        data object GoToHome : Navigation
    }

    sealed interface Event {
        data class FetchCategoryRecipes(val category: String) : Event
        data object GoToHome : Event
        data class GoToDetail(val id: String) : Event
    }
}
