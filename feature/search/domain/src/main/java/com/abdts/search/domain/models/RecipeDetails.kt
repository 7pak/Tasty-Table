package com.abdts.search.domain.models

data class RecipeDetails(
    val idMeal:String,
    val strArea:String,
    val strMeal:String,
    val strMealThumb: String,
    val strCategory:String,
    val strTags:String,
    val strYouTube:String,
    val strInstruction:String,
    val ingredientsPair:List<Pair<String,String>>
)
