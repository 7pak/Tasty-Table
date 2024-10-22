package com.abdts.search.domain.use_cases

import com.abdts.common.utils.NetworkResult
import com.abdts.search.domain.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetCategoryRecipes(
    private val searchRepository: SearchRepository
) {

    operator fun invoke(category:String) = flow {
        emit(NetworkResult.Loading())

        val response = searchRepository.getCategoryRecipes(category)
        if (response.isSuccess){
            emit(NetworkResult.Success(response.getOrThrow()))
        }else{
            emit(NetworkResult.Error(message = response.exceptionOrNull()?.localizedMessage))
        }

    }.catch {
        emit(NetworkResult.Error(it.message.toString() ))
    }.flowOn(Dispatchers.IO )
}