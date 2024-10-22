package com.abdts.recipeapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.abdts.common.navigation.NavigationSubGraphRoute

@Composable
fun RecipeNavigation(modifier:Modifier = Modifier,navigationSubGraph: NavigationSubGraph) {

    val navHostController = rememberNavController()

    NavHost(modifier = modifier,navController = navHostController, startDestination = NavigationSubGraphRoute.Main.route){

        navigationSubGraph.searchFeatureApi.registerGraph(navHostController = navHostController, navGraphBuilder = this)
        navigationSubGraph.mediaPlayerFeatureApi.registerGraph(navHostController = navHostController, navGraphBuilder = this)
    }
}