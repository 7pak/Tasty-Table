package com.abdts.search.ui.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.abdts.common.navigation.FeatureApi
import com.abdts.common.navigation.NavigationRoute
import com.abdts.common.navigation.NavigationSubGraphRoute
import com.abdts.search.ui.screens.category.CategoryModel
import com.abdts.search.ui.screens.category.CategoryRecipe
import com.abdts.search.ui.screens.category.CategoryScreen
import com.abdts.search.ui.screens.details.RecipeDetail
import com.abdts.search.ui.screens.details.RecipeDetailModel
import com.abdts.search.ui.screens.details.RecipeDetailsScreen
import com.abdts.search.ui.screens.favorite.FavoriteModel
import com.abdts.search.ui.screens.favorite.FavoriteScreen
import com.abdts.search.ui.screens.home.HomeModel
import com.abdts.search.ui.screens.home.HomeRecipe
import com.abdts.search.ui.screens.home.HomeScreen
import com.abdts.search.ui.screens.recipe_list.RecipeList
import com.abdts.search.ui.screens.recipe_list.RecipeListModel
import com.abdts.search.ui.screens.recipe_list.RecipeListScreen
import org.koin.androidx.compose.koinViewModel

interface SearchFeatureApi : FeatureApi

class SearchFeatureApiImpl : SearchFeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navHostController: NavHostController
    ) {
        navGraphBuilder.navigation(
            route = NavigationSubGraphRoute.Main.route,
            startDestination = NavigationRoute.Home.route
        ) {
            composable(route = NavigationRoute.Home.route) {
                val homeModel = koinViewModel<HomeModel>()
                HomeScreen(viewModel = homeModel,
                    navHostController = navHostController){
                    homeModel.onEvent(HomeRecipe.Event.GoToRecipeList)
                }
            }

            composable(route = NavigationRoute.RecipeList.route) {
                val recipeListModel = koinViewModel<RecipeListModel>()
                RecipeListScreen(
                    viewModel = recipeListModel,
                    navHostController = navHostController
                ) {
                    recipeListModel.onEvent(RecipeList.Event.GoToRecipeDetails(it))
                }
            }
            
            composable(route = NavigationRoute.Category.route) {navBackStackEntry->
                val categoryModel = koinViewModel<CategoryModel>()
                val category = navBackStackEntry.arguments?.getString("filter")
                LaunchedEffect(key1 = category) {
                    category?.let {
                        categoryModel.onEvent(CategoryRecipe.Event.FetchCategoryRecipes(category))
                    }
                }

                if (category != null) {
                    CategoryScreen(
                        viewModel = categoryModel,
                        navHostController = navHostController,
                        category = category
                    )
                }
            }

            composable(route = NavigationRoute.RecipeDetails.route) { navBackStackEntry ->
                val recipeDetailModel = koinViewModel<RecipeDetailModel>()
                val mealId = navBackStackEntry.arguments?.getString("id")
                val screen = navBackStackEntry.arguments?.getString("value")
                var fromRecipeListScreen = screen=="first"

                LaunchedEffect(key1 = mealId) {
                    mealId?.let {
                        recipeDetailModel.onEvent(RecipeDetail.Event.FetchRecipeDetails(mealId))
                        fromRecipeListScreen = screen=="first"
                    }
                }


                RecipeDetailsScreen(
                    viewModel = recipeDetailModel,
                    navHostController = navHostController,
                    fromRecipeListScreen = fromRecipeListScreen,
                    onNavigationClicked = {
                        recipeDetailModel.onEvent(RecipeDetail.Event.GoToRecipeListScreen)
                    },
                    onDelete = {
                        recipeDetailModel.onEvent(RecipeDetail.Event.DeleteRecipe(it))

                    }
                ) {
                    recipeDetailModel.onEvent(RecipeDetail.Event.InsertRecipe(it))
                }
            }

            composable(route = NavigationRoute.Favorite.route) {
                val favoriteModel = koinViewModel<FavoriteModel>()
                FavoriteScreen(
                    navHostController = navHostController,
                    viewModel = favoriteModel,
                    onClick = {
                        favoriteModel.onEvent(FavoriteScreen.Event.GoToDetails(it))
                    })
            }
        }
    }

}

