package com.abdts.recipeapp.navigation

import com.abdts.media_player.navigation.MediaPlayerFeatureApi
import com.abdts.search.ui.navigation.SearchFeatureApi

data class NavigationSubGraph(
    val searchFeatureApi: SearchFeatureApi,
    val mediaPlayerFeatureApi: MediaPlayerFeatureApi
)
