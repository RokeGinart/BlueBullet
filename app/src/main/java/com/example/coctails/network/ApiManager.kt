package com.example.coctails.network

import com.example.coctails.network.models.CocktailsCategoryList
import com.example.coctails.network.models.CocktailsSearch
import io.reactivex.Single

class ApiManager : CocktailsAPI {

    private val retrofit = RetrofitClient.getInstance.createRetrofit("https://www.thecocktaildb.com")
    private val appApi = retrofit.create(CocktailsAPI::class.java)

    override fun searchCocktails(cocktailName: String): Single<CocktailsCategoryList> {
       return appApi.searchCocktails(cocktailName)
    }

    override fun getCocktailsByCategory(cocktailCategory: String): Single<CocktailsCategoryList> {
        return appApi.getCocktailsByCategory(cocktailCategory)
    }

    override fun getCocktailById(cocktailId: String): Single<CocktailsSearch> {
        return appApi.getCocktailById(cocktailId)
    }
}