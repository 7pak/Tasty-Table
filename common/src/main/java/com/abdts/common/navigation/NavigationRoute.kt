package com.abdts.common.navigation


sealed class NavigationRoute(val route: String) {
    data object RecipeList : NavigationRoute(route = "/recipe_list")

    data object Home : NavigationRoute(route = "/home")

    data object Category : NavigationRoute(route = "/category/{filter}"){
        fun passCategory(category:String) = this.route.replace("{filter}",category)
    }

    data object RecipeDetails : NavigationRoute(route = "/recipe_details/{id}/{value}") {
        fun passId(id: String, screen: String = "first") =
            this.route.replace("{id}", id).replace("{value}", screen)
    }

    data object Favorite : NavigationRoute(route = "/favorite")

    data object MediaPlayer : NavigationRoute("/player/{video_id}") {
        fun passId(id: String) = this.route.replace("{video_id}", id)
    }
}

sealed class NavigationSubGraphRoute(val route: String) {
    data object Main : NavigationSubGraphRoute(route = "/main")
    data object MediaPlayer : NavigationSubGraphRoute(route = "/media_player")
}