package com.abdts.search.ui.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.abdts.common.navigation.NavigationRoute
import com.abdts.common.utils.UiText
import com.abdts.search.domain.models.RecipeDetails
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsScreen(
    viewModel: RecipeDetailModel,
    navHostController: NavHostController,
    fromRecipeListScreen:Boolean,
    onNavigationClicked: () -> Unit,
    onDelete: (RecipeDetails) -> Unit,
    onFavorite: (RecipeDetails) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = viewModel.navigation) {
        viewModel.navigation.flowWithLifecycle(lifecycleOwner.lifecycle).collectLatest {
            when (it) {
                RecipeDetail.Navigation.GoToRecipeListScreen -> navHostController.popBackStack()
                is RecipeDetail.Navigation.GoToMediaPlayer -> {
                    val videoUrl = it.videoUrl.split("v=").last()
                    navHostController.navigate(NavigationRoute.MediaPlayer.passId(videoUrl))
                }
            }
        }
    }

    var favorite by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = uiState.data?.strMeal.toString(),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
                navigationIcon = {
                    IconButton(onClick = onNavigationClicked) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }, actions = {
                    if (fromRecipeListScreen) {
                        IconButton(onClick = {
                            uiState.data?.let {
                                onFavorite(it)
                                favorite = true
                            }
                        }) {
                            Icon(
                                imageVector = if (favorite) Icons.Filled.Star else Icons.Default.StarBorder,
                                contentDescription = null
                            )
                        }
                    }
                    if (!fromRecipeListScreen){
                        IconButton(onClick = {
                            uiState.data?.let {
                                onDelete(it)
                                navHostController.popBackStack()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null
                            )
                        }
                    }

                })
        }
    ) { innerPadding ->

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (uiState.error !is UiText.Idle) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(text = uiState.error.getString())
            }
        }

        uiState.data?.let { recipeDetails ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = recipeDetails.strMealThumb,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray
                        )
                    )
                    Spacer(modifier = Modifier.height(7.dp))

                    Text(
                        text = recipeDetails.strInstruction,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(17.dp))

                    Text(
                        text = "Ingredients",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(7.dp))
                    recipeDetails.ingredientsPair.forEach {
                        if (it.first.isNotEmpty()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(CircleShape)
                                    .padding(horizontal = 12.dp)
                                    .background(
                                        color = Color.Gray.copy(alpha = .1f),
                                        shape = CircleShape
                                    )
                                    .padding(8.dp)
                                ,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = getIngredientImageUrl(it.first),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)

                                        .background(
                                            Color.White, shape = CircleShape
                                        )
                                        .padding(5.dp)
                                )
                                Text(
                                    text = it.second,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                        }

                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    if (recipeDetails.strYouTube.isNotEmpty()) {
                        Button(
                            onClick = {
                                viewModel.onEvent(RecipeDetail.Event.GoToMediaPlayer(recipeDetails.strYouTube))
                            },
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(imageVector = Icons.Default.PlayCircle, contentDescription =null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Watch YouTube Video")
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }


            }


        }
    }
}

private fun getIngredientImageUrl(name: String) =
    "https://www.themealdb.com/images/ingredients/$name.png"
