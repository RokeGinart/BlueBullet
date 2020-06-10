package com.example.coctails.ui.screens.fragments.cocktaildetails

import com.example.coctails.core.room.entity.cocktails_data.CocktailFirebaseData
import com.example.coctails.ui.BasePresenter

abstract class CocktailDetailsPresenter : BasePresenter<CocktailDetailsView>(){

    abstract fun getFavorite(cocktailId: Int, category: String)
    abstract fun saveCocktailToFavorite(cocktailId: Int, name: String, image: String, category: String, abv: Int, categoryName: String, favorite: Boolean)
    abstract fun getIngredientDetail(cocktails: CocktailFirebaseData)
    abstract fun setIngredientToDB(ingredientId: Int, category: String)
}