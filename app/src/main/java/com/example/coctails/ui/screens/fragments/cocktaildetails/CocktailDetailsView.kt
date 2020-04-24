package com.example.coctails.ui.screens.fragments.cocktaildetails

import com.example.coctails.network.models.CocktailsSearch
import com.example.coctails.ui.BaseView

interface CocktailDetailsView : BaseView {
    fun showResult(cocktails: CocktailsSearch)
}