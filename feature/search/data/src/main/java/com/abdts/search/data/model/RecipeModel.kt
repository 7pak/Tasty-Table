package com.abdts.search.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeModel<T>(
    @SerialName("meals")
    val recipes: List<T>?=null
)