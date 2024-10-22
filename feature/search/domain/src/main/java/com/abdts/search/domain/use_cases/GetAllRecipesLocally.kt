package com.abdts.search.domain.use_cases

import com.abdts.search.domain.repository.SearchRepository

class GetAllRecipesLocally(
    private val searchRepository: SearchRepository
) {

    operator fun invoke() = searchRepository.getAllRecipes()
}