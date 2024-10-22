package com.abdts.search.ui.di

import com.abdts.search.ui.navigation.SearchFeatureApi
import com.abdts.search.ui.navigation.SearchFeatureApiImpl
import com.abdts.search.ui.screens.category.CategoryModel
import com.abdts.search.ui.screens.details.RecipeDetailModel
import com.abdts.search.ui.screens.favorite.FavoriteModel
import com.abdts.search.ui.screens.home.HomeModel
import com.abdts.search.ui.screens.recipe_list.RecipeListModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    single <SearchFeatureApi>{
        SearchFeatureApiImpl()
    }
    viewModelOf(::RecipeListModel)
    viewModelOf(::RecipeDetailModel)
    viewModelOf(::FavoriteModel)
    viewModelOf(::HomeModel)
    viewModelOf(::CategoryModel)
}