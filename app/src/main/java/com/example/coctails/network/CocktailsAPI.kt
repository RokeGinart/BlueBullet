package com.example.coctails.network

import com.example.coctails.network.models.CocktailsSearch
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailsAPI {

    @GET("api/json/v1/1/search.php")
    fun searchCocktails(@Query("s") cocktailName : String) : Single<CocktailsSearch>
}