package com.abdts.search.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import com.abdts.common.data.AppData
import com.abdts.common.navigation.NavigationRoute
import com.abdts.common.utils.UiText
import com.abdts.search.ui.screens.home.item.CategoryItem
import com.abdts.search.ui.screens.home.item.EmptyFavoritesAlert
import com.abdts.search.ui.screens.home.item.PremiumAd
import com.abdts.search.ui.screens.home.item.RecipeCard
import com.abdts.search.ui.screens.home.item.RecipeLogo
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(viewModel: HomeModel, navHostController: NavHostController, onClick: () -> Unit) {


    val uiState by viewModel.uiState.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel.navigation) {
        viewModel.navigation.flowWithLifecycle(lifecycleOwner.lifecycle).collectLatest {
            when (it) {
                HomeRecipe.Navigation.GoToRecipeList -> {
                    navHostController.navigate(NavigationRoute.RecipeList.route)
                }

                HomeRecipe.Navigation.GoToFavorite -> {
                    navHostController.navigate(NavigationRoute.Favorite.route)
                }

                is HomeRecipe.Navigation.GoToDetails -> {
                    navHostController.navigate(NavigationRoute.RecipeDetails.passId(it.id, screen = "second"))
                }

                is HomeRecipe.Navigation.GoToCategory -> {
                    navHostController.navigate(NavigationRoute.Category.passCategory(it.category))
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        RecipeLogo(modifier = Modifier.weight(5f))

        Spacer(modifier = Modifier.weight(3f))

        Column(
            modifier = Modifier
                .weight(20f)
                .fillMaxWidth()
        ) {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                modifier = Modifier.padding(horizontal = 7.dp)
            )
            LazyRow(
                modifier = Modifier
                    .padding(7.dp)
            ) {
                items(AppData.categories) { category ->
                    val name = category.first
                    val image = category.second

                    CategoryItem(name = name, image = image) {
                        viewModel.onEvent(HomeRecipe.Event.GoToCategory(name))
                    }
                }
            }
        }

        Text(
            text = "I would like to cook...",
            style = MaterialTheme.typography.displaySmall.copy(
                color = Color.DarkGray,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.weight(8f)
        )

        Box(modifier = Modifier
            .weight(7f)
            .fillMaxWidth(0.9f)
        ) {
            TextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxSize(),
                shape = CircleShape,
            readOnly = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Gray.copy(alpha = .2f),
                unfocusedIndicatorColor = Color.White,
            ),
            placeholder = { Text(text = "Search for recipe...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
            )

            Box(modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .clickable { onClick() })
        }
        Spacer(modifier = Modifier.weight(5f))


        PremiumAd(modifier = Modifier.weight(18f))

        Spacer(modifier = Modifier.weight(3f))

        Column(
            modifier = Modifier
                .weight(37f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Favorite Recipes",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal)
                )

                TextButton(onClick = {viewModel.onEvent(HomeRecipe.Event.GoToFavorite) }) {
                    Text(text = "View All", color = Color.Gray)
                }

            }
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            if (uiState.error !is UiText.Idle) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Text(text = uiState.error.getString(LocalContext.current))
                }
            }

            uiState.data?.let {list->
                if (list.isEmpty()){
                    EmptyFavoritesAlert()
                }else{
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        items(list.takeLast(3)) {
                            RecipeCard(
                                modifier = Modifier
                                    .weight(10f)
                                    .padding(7.dp),
                                name = it.strMeal,
                                image = it.strMealThumb
                            ){
                                viewModel.onEvent(HomeRecipe.Event.GoToDetails(it.idMeal))
                            }
                        }
                    }
                }
            }
        }
    }
}