package com.example.coctails.ui.screens.fragments.cocktailscategory

import com.example.coctails.ui.BasePresenter

abstract class CocktailsCategoryPresenter : BasePresenter<CocktailsCategoryView>() {
    abstract fun getCocktailsByCategory(category : String)
}