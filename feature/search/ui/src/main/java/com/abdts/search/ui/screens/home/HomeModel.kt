package com.abdts.search.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdts.common.utils.UiText
import com.abdts.search.domain.models.Recipe
import com.abdts.search.domain.use_cases.GetAllRecipesLocally
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeModel(
    private val getAllRecipesLocally: GetAllRecipesLocally,
    ):ViewModel() {

    private val _uiState = MutableStateFlow(HomeRecipe.UiState())
    val uiState: StateFlow<HomeRecipe.UiState> = _uiState.asStateFlow()

    private val _navigation = Channel<HomeRecipe.Navigation>()
    val navigation: Flow<HomeRecipe.Navigation> = _navigation.receiveAsFlow()


    init {
        getRecipesList()
    }

    private fun getRecipesList() = viewModelScope.launch {
        getAllRecipesLocally.invoke().collectLatest { recipeList ->
            _uiState.update {
                HomeRecipe.UiState(data = recipeList)
            }
        }
    }


    fun onEvent(event: HomeRecipe.Event){
        when(event){

            is HomeRecipe.Event.GoToRecipeList -> {
                viewModelScope.launch {
                    _navigation.send(HomeRecipe.Navigation.GoToRecipeList)
                }
            }

            HomeRecipe.Event.GoToFavorite -> {
                viewModelScope.launch {
                    _navigation.send(HomeRecipe.Navigation.GoToFavorite)
                }
            }

            is HomeRecipe.Event.GoToDetails -> {
                viewModelScope.launch {
                    _navigation.send(HomeRecipe.Navigation.GoToDetails(event.id))
                }
            }

            is HomeRecipe.Event.GoToCategory -> {
                viewModelScope.launch {
                    _navigation.send(HomeRecipe.Navigation.GoToCategory(event.category))
                }
            }
        }
    }
}

object HomeRecipe{
    data class UiState(
        val isLoading:Boolean = false,
        val error: UiText = UiText.Idle,
        val data:List<Recipe>?=null
    )

    sealed interface Navigation{
        data object GoToRecipeList:Navigation
        data object GoToFavorite: Navigation
        data class GoToDetails(val id: String) : Navigation
        data class GoToCategory(val category: String) : Navigation



    }

    sealed interface Event{
        data object GoToRecipeList:Event
        data object GoToFavorite: Event
        data class GoToDetails(val id: String) : Event
        data class GoToCategory(val category: String) : Event

    }
}