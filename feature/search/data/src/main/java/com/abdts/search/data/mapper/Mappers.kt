package com.abdts.search.data.mapper

import com.abdts.search.data.model.RecipeCategoryModel
import com.abdts.search.data.model.RecipeDto
import com.abdts.search.domain.models.Recipe
import com.abdts.search.domain.models.RecipeCategory
import com.abdts.search.domain.models.RecipeDetails

fun List<RecipeDto>.toRecipeDomain():List<Recipe> = map {
    Recipe(
        idMeal = it.idMeal?:"",
        strArea = it.strArea?:"",
        strMeal = it.strMeal?:"",
        strMealThumb = it.strMealThumb?:"",
        strCategory = it.strCategory?:"",
        strTags = it.strTags?:"",
        strYouTube = it.strYoutube?:"",
        strInstruction = it.strInstructions?:""
    )
}

fun RecipeDto.toDomain():RecipeDetails{
    return RecipeDetails(
        idMeal = idMeal?:"",
        strArea = strArea?:"",
        strMeal = strMeal?:"",
        strMealThumb = strMealThumb?:"",
        strCategory = strCategory?:"",
        strTags = strTags?:"",
        strYouTube = strYoutube?:"",
        strInstruction = strInstructions?:"",
        ingredientsPair = this.getIngredientPair()
    )
}

fun List<RecipeCategoryModel>.toDomain():List<RecipeCategory> = map {
     RecipeCategory(
        idMeal = it.idMeal,
        strMeal = it.strMeal,
        strMealThumb = it.strMealThumb
    )
}

fun RecipeDto.getIngredientPair():List<Pair<String,String>> {
    val list = mutableListOf<Pair<String,String>>()
    list.apply {
        add(Pair(strIngredient1.orEmpty(), strMeasure1.orEmpty()))
        add(Pair(strIngredient2.orEmpty(), strMeasure2.orEmpty()))
        add(Pair(strIngredient3.orEmpty(), strMeasure3.orEmpty()))
        add(Pair(strIngredient4.orEmpty(), strMeasure4.orEmpty()))
        add(Pair(strIngredient5.orEmpty(), strMeasure5.orEmpty()))
        add(Pair(strIngredient6.orEmpty(), strMeasure6.orEmpty()))
        add(Pair(strIngredient7.orEmpty(), strMeasure7.orEmpty()))
        add(Pair(strIngredient8.orEmpty(), strMeasure8.orEmpty()))
        add(Pair(strIngredient9.orEmpty(), strMeasure9.orEmpty()))
        add(Pair(strIngredient10.orEmpty(), strMeasure10.orEmpty()))
        add(Pair(strIngredient11.orEmpty(), strMeasure11.orEmpty()))
        add(Pair(strIngredient12.orEmpty(), strMeasure12.orEmpty()))
        add(Pair(strIngredient13.orEmpty(), strMeasure13.orEmpty()))
        add(Pair(strIngredient14.orEmpty(), strMeasure14.orEmpty()))
        add(Pair(strIngredient15.orEmpty(), strMeasure15.orEmpty()))
        add(Pair(strIngredient16.orEmpty(), strMeasure16.orEmpty()))
        add(Pair(strIngredient17.orEmpty(), strMeasure17.orEmpty()))
        add(Pair(strIngredient18.orEmpty(), strMeasure18.orEmpty()))
        add(Pair(strIngredient19.orEmpty(), strMeasure19.orEmpty()))
        add(Pair(strIngredient20.orEmpty(), strMeasure20.orEmpty()))
    }

    return  list
}

