package com.example.coctails.ui.screens.fragments.cocktaildetails

import com.example.coctails.network.models.firebase.drink.Cocktails
import com.example.coctails.ui.BaseView

interface CocktailDetailsView : BaseView {
    fun showResult(cocktails: Cocktails)
}