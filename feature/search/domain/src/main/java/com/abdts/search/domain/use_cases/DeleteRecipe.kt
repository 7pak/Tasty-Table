package com.abdts.search.domain.use_cases

import com.abdts.search.domain.models.Recipe
import com.abdts.search.domain.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DeleteRecipe(
    private val searchRepository: SearchRepository
) {

    operator fun invoke(recipe: Recipe) = flow<Unit> {

        searchRepository.deleteRecipe(recipe)

    }.flowOn(Dispatchers.IO)
}