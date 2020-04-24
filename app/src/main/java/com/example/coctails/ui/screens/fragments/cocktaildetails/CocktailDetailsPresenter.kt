package com.example.coctails.ui.screens.fragments.cocktaildetails

import com.example.coctails.ui.BasePresenter

abstract class CocktailDetailsPresenter : BasePresenter<CocktailDetailsView>(){

    abstract fun getCocktailDetails(category: String, cocktailsId : String)
}