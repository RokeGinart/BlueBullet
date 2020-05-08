package com.example.coctails.ui.screens.fragments.favorites

import com.example.coctails.ui.BasePresenter

abstract class FavoritePresenter : BasePresenter<FavoriteView>(){

    abstract fun getFavoriteList()
    abstract fun getSelectedCocktail(category : String, id : Int)
    abstract fun setFavoriteStatus (favorite: Boolean, cocktailId : Int, category : String)
}