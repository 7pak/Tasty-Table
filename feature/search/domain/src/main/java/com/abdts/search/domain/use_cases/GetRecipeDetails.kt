package com.abdts.search.domain.use_cases

import com.abdts.common.utils.NetworkResult
import com.abdts.search.domain.models.RecipeDetails
import com.abdts.search.domain.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetRecipeDetails(
    private val searchRepository: SearchRepository
) {

    operator fun invoke(id:String) = flow<NetworkResult<RecipeDetails>> {
        emit(NetworkResult.Loading())

        val response = searchRepository.getRecipeDetails(id)
        if (response.isSuccess){
            emit(NetworkResult.Success(response.getOrThrow()))
        }else{
            emit(NetworkResult.Error(message = response.exceptionOrNull()?.localizedMessage))
        }

    }.catch {
        emit(NetworkResult.Error(it.message.toString() ))
    }.flowOn(Dispatchers.IO )
}