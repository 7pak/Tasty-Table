package com.abdts.search.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipe(
    @PrimaryKey(autoGenerate = false)
    val idMeal:String,
    val strArea:String,
    val strMeal:String,
    val strMealThumb: String,
    val strCategory:String,
    val strTags:String,
    val strYouTube:String,
    val strInstruction:String
)


