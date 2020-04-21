package com.example.coctails.network

import com.example.coctails.network.models.CocktailsSearch
import io.reactivex.Single

class ApiManager : CocktailsAPI {

    private val retrofit = RetrofitClient.getInstance.createRetrofit("https://www.thecocktaildb.com")
    private val appApi = retrofit.create(CocktailsAPI::class.java)


    override fun searchCocktails(cocktailName: String): Single<CocktailsSearch> {
       return appApi.searchCocktails(cocktailName)
    }

    /*   override fun getTopMovie(): Single<List<TopMovies>> {
        return appApi.getTopMovie()
    }

    override fun getLatestUpdate(page: Int): Single<LatestUpdate> {
        return appApi.getLatestUpdate(page)
    }
    */
}