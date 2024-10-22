package com.abdts.search.ui.screens.favorite

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.abdts.common.navigation.NavigationRoute
import com.abdts.common.utils.UiText
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FavoriteScreen(
    navHostController: NavHostController,
    viewModel: FavoriteModel,
    onClick:(String)->Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = viewModel.navigation) {
        viewModel.navigation.flowWithLifecycle(lifecycleOwner.lifecycle).collectLatest {
            when(it){
                is FavoriteScreen.Navigation.GotoRecipeDetailScreen -> {
                    navHostController.navigate(NavigationRoute.RecipeDetails.passId(it.id, screen = "second"))
                }
            }
        }
    }

    var showDropDown by rememberSaveable {
        mutableStateOf(false)
    }

    var selectedIndex by rememberSaveable {
        mutableIntStateOf(-1)
    }
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "Favorite Recipes", style = MaterialTheme.typography.headlineSmall)
            }, actions = {
                IconButton(onClick = {
                    showDropDown = !showDropDown
                }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                }
                
                if(showDropDown){
                    DropdownMenu(expanded = showDropDown, onDismissRequest = {
                        showDropDown = showDropDown.not()
                    }) {
                        DropdownMenuItem(text = { 
                                                Text(text = "Alphabetical")
                        }, onClick = {
                            selectedIndex = 0
                            showDropDown = showDropDown.not()
                            viewModel.onEvent(FavoriteScreen.Event.AlphabeticalSort)
                        }, leadingIcon = {
                            RadioButton(selected = selectedIndex ==0, onClick = {
                                selectedIndex = 0
                                showDropDown = showDropDown.not()
                                viewModel.onEvent(FavoriteScreen.Event.AlphabeticalSort)
                            })
                        })
                        DropdownMenuItem(text = {
                                                Text(text = "Less Ingredient")
                        }, onClick = {
                            selectedIndex = 1
                            showDropDown = showDropDown.not()
                            viewModel.onEvent(FavoriteScreen.Event.LessIngredientSort)
                        }, leadingIcon = {
                            RadioButton(selected = selectedIndex ==1, onClick = {
                                selectedIndex = 1
                                showDropDown = showDropDown.not()
                                viewModel.onEvent(FavoriteScreen.Event.LessIngredientSort)
                            })
                        })
                        DropdownMenuItem(text = {
                                                Text(text = "Reset")
                        }, onClick = {
                            selectedIndex = 2
                            showDropDown = showDropDown.not()
                            viewModel.onEvent(FavoriteScreen.Event.ResetSort)
                        }, leadingIcon = {
                            RadioButton(selected = selectedIndex ==2, onClick = {
                                selectedIndex = 2
                                showDropDown = showDropDown.not()
                                viewModel.onEvent(FavoriteScreen.Event.ResetSort)
                            })
                        })
                    }
                }
            }

            )
        }
    ) {innerPadding->
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

        uiState.data?.let {list->
            if (list.isEmpty()){
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Text(text = "No data found")
                }
            }else{
                LazyColumn(modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()) {
                    items(list){recipe->
                        Card(
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal = 12.dp)
                                .clickable {
                                    onClick(recipe.idMeal)
                                },
                            shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = .1f))
                        ) {
                            Box(modifier = Modifier.fillMaxWidth()){
                                AsyncImage(
                                    model = recipe.strMealThumb,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(250.dp),
                                    contentScale = ContentScale.Crop
                                )
                                IconButton(onClick = {
                                                     viewModel.onEvent(FavoriteScreen.Event.DeleteRecipe(recipe))
                                },modifier = Modifier.background(Color.Red, shape = RoundedCornerShape(bottomEnd = 12.dp))) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription =null, tint = Color.White)
                                }
                            }


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
                                                    style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
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

}