package com.abdts.search.data.data_source.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse

class SearchApi (
    private val httpClient: HttpClient,
    private val url:String
){

    suspend fun getRecipes(query:String):HttpResponse{
        return httpClient.get(
            urlString = "$url/api/json/v1/1/search.php"
        ){
            parameter("s",query)
        }
    }

    suspend fun getRecipeDetails(id:String):HttpResponse{
        return httpClient.get(
            urlString = "$url/api/json/v1/1/lookup.php"
        ){
            parameter("i",id)
        }
    }
    suspend fun getCategoryRecipes(category:String):HttpResponse{
        return httpClient.get(
            urlString = "$url/api/json/v1/1/filter.php"
        ){
            parameter("c",category)
        }
    }
}