package com.abdts.search.ui.screens.recipe_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.abdts.common.navigation.NavigationRoute
import com.abdts.common.utils.UiText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecipeListScreen(
    viewModel: RecipeListModel,
    navHostController: NavHostController,
    onClick: (String) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()


    val query = rememberSaveable {
        mutableStateOf("")
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel.navigation) {
        viewModel.navigation.flowWithLifecycle(lifecycleOwner.lifecycle).collectLatest {
            when (it) {
                is RecipeList.Navigation.GoToRecipeDetails -> {
                    navHostController.navigate(NavigationRoute.RecipeDetails.passId(it.id))
                }
            }
        }
    }


    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(300)
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            TextField(
                value = query.value,
                onValueChange = {
                    query.value = it
                    viewModel.onEvent(RecipeList.Event.SearchRecipe(query.value))
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ), modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),

            )
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
                                onClick(recipe.idMeal)
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
                            Text(text = recipe.strMeal, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal))
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = recipe.strInstruction,
                                style = MaterialTheme.typography.bodyMedium,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 4
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            if (recipe.strTags.isNotEmpty()) {
                                FlowRow {
                                    recipe.strTags.split(",").forEach {
                                        Box(
                                            modifier = Modifier
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                                .wrapContentSize()
                                                .background(
                                                    Color.White,
                                                    shape = RoundedCornerShape(24.dp)
                                                )
                                                .clip(
                                                    RoundedCornerShape(24.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = it,
                                                style = MaterialTheme.typography.bodySmall,
                                                modifier = Modifier.padding(
                                                    vertical = 6.dp,
                                                    horizontal = 12.dp
                                                )
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }

                    }
                }
            }
        }
    }
}