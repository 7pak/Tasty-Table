package com.abdts.media_player.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.abdts.common.navigation.FeatureApi
import com.abdts.common.navigation.NavigationRoute
import com.abdts.common.navigation.NavigationSubGraphRoute
import com.abdts.media_player.screens.MediaPlayerScreen

interface MediaPlayerFeatureApi:FeatureApi


class MediaPlayerFeatureApiImpl:MediaPlayerFeatureApi{
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navHostController: NavHostController
    ) {
        navGraphBuilder.navigation(route =NavigationSubGraphRoute.MediaPlayer.route, startDestination = NavigationRoute.MediaPlayer.route){
            composable(route = NavigationRoute.MediaPlayer.route){ navBackStackEntry ->
                val videoId = navBackStackEntry.arguments?.getString("video_id")
                videoId?.let {
                    MediaPlayerScreen(videoId = it)
                }
            }
        }
    }
}