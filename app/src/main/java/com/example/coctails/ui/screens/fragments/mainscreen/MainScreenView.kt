package com.example.coctails.ui.screens.fragments.mainscreen

import com.example.coctails.network.models.CocktailsCategoryList
import com.example.coctails.ui.BaseView

interface MainScreenView : BaseView {

    fun showAllCocktails(cocktailsCategoryList: List<CocktailsCategoryList.Drink>?)
    fun showSearchResult(cocktailsSearch: List<CocktailsCategoryList.Drink>?)
}