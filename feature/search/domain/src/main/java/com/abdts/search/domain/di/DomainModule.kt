package com.abdts.search.domain.di

import com.abdts.search.domain.use_cases.DeleteRecipe
import com.abdts.search.domain.use_cases.GetAllRecipesLocally
import com.abdts.search.domain.use_cases.GetAllRecipesRemotely
import com.abdts.search.domain.use_cases.GetCategoryRecipes
import com.abdts.search.domain.use_cases.GetRecipeDetails
import com.abdts.search.domain.use_cases.InsertRecipe
import org.koin.dsl.module

val domainModule = module {
    factory {
        GetRecipeDetails(get())
    }
    factory {
        GetAllRecipesRemotely(get())
    }
    factory {
        GetCategoryRecipes(get())
    }
    factory {
        GetAllRecipesLocally(get())
    }
    factory {
        InsertRecipe(get())
    }
    factory {
        DeleteRecipe(get())
    }
}