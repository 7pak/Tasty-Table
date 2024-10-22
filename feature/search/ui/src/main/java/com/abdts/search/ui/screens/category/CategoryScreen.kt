package com.abdts.search.ui.screens.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.abdts.common.navigation.NavigationRoute
import com.abdts.common.utils.UiText
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    viewModel: CategoryModel,
    navHostController: NavHostController,
    category: String
) {

    val uiState by viewModel.uiState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel.navigation) {
        viewModel.navigation.flowWithLifecycle(lifecycleOwner.lifecycle).collectLatest {
            when (it) {
                is CategoryRecipe.Navigation.GoToDetail -> {
                    navHostController.navigate(NavigationRoute.RecipeDetails.passId(it.id))
                }

                CategoryRecipe.Navigation.GoToHome -> {
                    navHostController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = category,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(CategoryRecipe.Event.GoToHome) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
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
                Text(text = uiState.error.getString(LocalContext.current))
            }
        }

        uiState.data?.let { list ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(list) { recipe ->
                    Card(
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 12.dp)
                            .clickable {
                                viewModel.onEvent(CategoryRecipe.Event.GoToDetail(recipe.idMeal))
                            },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        AsyncImage(
                            model = recipe.strMealThumb,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = recipe.strMeal,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                    }
                }
            }
        }
    }
}