package com.abdts.search.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeCategoryModel(
    @SerialName("idMeal")
    val idMeal: String,
    @SerialName("strMeal")
    val strMeal: String,
    @SerialName("strMealThumb")
    val strMealThumb: String
)