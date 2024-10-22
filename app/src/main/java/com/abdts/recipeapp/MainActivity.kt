package com.abdts.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.abdts.recipeapp.navigation.NavigationSubGraph
import com.abdts.recipeapp.navigation.RecipeNavigation
import com.abdts.recipeapp.ui.theme.RecipeAppTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navigationSubGraph by inject<NavigationSubGraph>()
        setContent {
            RecipeAppTheme {
                Surface(modifier = Modifier.safeContentPadding()) {
                    RecipeNavigation(navigationSubGraph = navigationSubGraph)
                }
            }
        }
    }
}
