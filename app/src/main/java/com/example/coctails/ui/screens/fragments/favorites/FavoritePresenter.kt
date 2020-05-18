package com.example.coctails.ui.screens.fragments.favorites

import com.example.coctails.ui.BasePresenter

abstract class FavoritePresenter : BasePresenter<FavoriteView>(){

    abstract fun getFavoriteList()
    abstract fun getSelectedCocktail(category : String, id : Int)
    abstract fun setFavoriteStatus (cocktailId: Int, name: String, image: String, category: String, abv: Int, categoryName: String, favorite: Boolean)
}